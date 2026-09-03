package web.route;

import com.sun.net.httpserver.HttpExchange;
import web.ApiResult;
import web.JsonUtil;
import web.SessionManager;
import web.SessionManager.SessionData;
import dao.AppointmentDAO;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class DoctorResource {

    private static final AppointmentDAO appointmentDAO = new AppointmentDAO();

    public static void getDoctorAppointments(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, ApiResult.error("Method not allowed"));
            return;
        }
        SessionData session = SessionManager.requireAuth(exchange);
        if (session == null) {
            sendResponse(exchange, 401, ApiResult.error("Not authenticated"));
            return;
        }
        if (!"Doctor".equals(session.role)) {
            sendResponse(exchange, 403, ApiResult.error("Access denied"));
            return;
        }

        int doctorId = appointmentDAO.getDoctorIdByUsername(session.username);
        if (doctorId <= 0) {
            sendResponse(exchange, 404, ApiResult.error("Doctor not found"));
            return;
        }

        var list = appointmentDAO.getAppointmentsByDoctorId(doctorId);
        sendResponse(exchange, 200, ApiResult.ok(AdminResource.appointmentsToListMap(list)));
    }

    public static void savePrescription(HttpExchange exchange, int appointmentId) throws IOException {
        if (!"PUT".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, ApiResult.error("Method not allowed"));
            return;
        }
        SessionData session = SessionManager.requireAuth(exchange);
        if (session == null) {
            sendResponse(exchange, 401, ApiResult.error("Not authenticated"));
            return;
        }
        if (!"Doctor".equals(session.role)) {
            sendResponse(exchange, 403, ApiResult.error("Access denied"));
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");
        Map<String, Object> req = JsonUtil.parseObject(body);
        String treatment = JsonUtil.getString(req, "treatment", "").trim();

        if (treatment.isEmpty()) {
            sendResponse(exchange, 400, ApiResult.error("Treatment selection is required."));
            return;
        }

        boolean saved = appointmentDAO.updatePrescription(appointmentId, treatment);
        if (saved) {
            double fee = appointmentDAO.getTreatmentFee(treatment);
            if (fee >= 0) {
                appointmentDAO.updateFee(appointmentId, fee);
            }
            sendResponse(exchange, 200, ApiResult.ok("Prescription saved: " + treatment, null));
        } else {
            sendResponse(exchange, 500, ApiResult.error("Failed to save prescription."));
        }
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
