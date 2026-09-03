package web.route;

import com.sun.net.httpserver.HttpExchange;
import web.ApiResult;
import web.JsonUtil;
import web.NotificationService;
import web.SessionManager;
import web.SessionManager.SessionData;
import Controller.BillingController;
import dao.AppointmentDAO;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class ReceptionistResource {

    private static final AppointmentDAO appointmentDAO = new AppointmentDAO();

    public static void getAll(HttpExchange exchange) throws IOException {
        if (!checkRole(exchange, "Receptionist")) return;
        var list = appointmentDAO.getAllAppointments();
        sendResponse(exchange, 200, ApiResult.ok(AdminResource.appointmentsToListMap(list)));
    }

    public static void search(HttpExchange exchange) throws IOException {
        if (!checkRole(exchange, "Receptionist")) return;
        String query = exchange.getRequestURI().getQuery();
        String keyword = "";
        if (query != null) {
            for (String param : query.split("&")) {
                String[] kv = param.split("=", 2);
                if (kv.length == 2 && "q".equals(kv[0])) {
                    keyword = java.net.URLDecoder.decode(kv[1], "UTF-8");
                }
            }
        }
        if (keyword.isEmpty()) {
            sendResponse(exchange, 400, ApiResult.error("Search keyword is required."));
            return;
        }
        var list = appointmentDAO.searchAppointments(keyword);
        sendResponse(exchange, 200, ApiResult.ok(AdminResource.appointmentsToListMap(list)));
    }

    public static void updateVisitType(HttpExchange exchange, int appointmentId) throws IOException {
        if (!checkRole(exchange, "Receptionist")) return;
        String body = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");
        Map<String, Object> req = JsonUtil.parseObject(body);
        String visitType = JsonUtil.getString(req, "visitType", "").trim();

        if (visitType.isEmpty()) {
            sendResponse(exchange, 400, ApiResult.error("Visit type is required."));
            return;
        }

        boolean success = appointmentDAO.updateVisitType(appointmentId, visitType);
        if (success) {
            sendResponse(exchange, 200, ApiResult.ok("Visit type updated", null));
        } else {
            sendResponse(exchange, 500, ApiResult.error("Failed to update visit type."));
        }
    }

    public static void calculateBill(HttpExchange exchange) throws IOException {
        if (!checkRole(exchange, "Receptionist")) return;
        String body = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");
        Map<String, Object> req = JsonUtil.parseObject(body);
        int appointmentId = JsonUtil.getInt(req, "appointmentId", -1);

        if (appointmentId <= 0) {
            sendResponse(exchange, 400, ApiResult.error("Valid appointment ID is required."));
            return;
        }

        var appointment = appointmentDAO.getAppointmentById(appointmentId);
        if (appointment == null) {
            sendResponse(exchange, 404, ApiResult.error("Appointment not found."));
            return;
        }

        String treatment = appointment.getTreatmentPrescribed();
        if (treatment == null || treatment.isEmpty()) {
            sendResponse(exchange, 400, ApiResult.error("No prescription yet. Doctor must prescribe treatment first."));
            return;
        }

        BillingController billingController = new BillingController();
        double baseFee = appointment.getFee();
        if (baseFee == 0.0) {
            baseFee = billingController.getBaseFee(treatment);
            billingController.updateFee(appointmentId, baseFee);
        }

        String bill = billingController.buildBill(
            appointment.getPatientName(), appointment.getVisitType(), treatment, baseFee);

        java.util.LinkedHashMap<String, Object> data = new java.util.LinkedHashMap<>();
        data.put("billText", bill);
        data.put("patientName", appointment.getPatientName());
        data.put("visitType", appointment.getVisitType());
        data.put("treatment", treatment);
        data.put("baseFee", baseFee);
        double tax = billingController.isGeneralVisit(treatment) ? 0.0 : baseFee * 0.05;
        data.put("tax", tax);
        data.put("total", baseFee + tax);

        dao.PatientDAO patientDAO = new dao.PatientDAO();
        Model.Patient patient = patientDAO.getPatientByName(appointment.getPatientName());
        String notification = null;
        if (patient != null && patient.getEmail() != null && !patient.getEmail().trim().isEmpty()) {
            notification = NotificationService.sendBillReady(
                patient.getEmail(), appointment.getPatientName(), treatment, baseFee, tax, baseFee + tax);
        }
        data.put("notification", notification);

        sendResponse(exchange, 200, ApiResult.ok(data));
    }

    private static boolean checkRole(HttpExchange exchange, String requiredRole) throws IOException {
        SessionData session = SessionManager.requireAuth(exchange);
        if (session == null) {
            sendResponse(exchange, 401, ApiResult.error("Not authenticated"));
            return false;
        }
        if (!requiredRole.equals(session.role)) {
            sendResponse(exchange, 403, ApiResult.error("Access denied"));
            return false;
        }
        return true;
    }

    static void sendResponse(HttpExchange exchange, int status, ApiResult result) throws IOException {
        byte[] bytes = result.toJson().getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
