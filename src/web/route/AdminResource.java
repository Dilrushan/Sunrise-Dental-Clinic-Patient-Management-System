package web.route;

import com.sun.net.httpserver.HttpExchange;
import web.ApiResult;
import web.SessionManager;
import web.SessionManager.SessionData;
import dao.AppointmentDAO;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdminResource {

    private static final AppointmentDAO appointmentDAO = new AppointmentDAO();

    public static void getAll(HttpExchange exchange) throws IOException {
        if (!checkRole(exchange, "Admin")) return;
        List<Model.Appointment> list = appointmentDAO.getAllAppointments();
        sendResponse(exchange, 200, ApiResult.ok(appointmentsToListMap(list)));
    }

    public static void search(HttpExchange exchange) throws IOException {
        if (!checkRole(exchange, "Admin")) return;
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
        List<Model.Appointment> list = appointmentDAO.searchAppointments(keyword);
        sendResponse(exchange, 200, ApiResult.ok(appointmentsToListMap(list)));
    }

    public static void updateDate(HttpExchange exchange, int appointmentId) throws IOException {
        if (!checkRole(exchange, "Admin")) return;
        String body = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");
        Map<String, Object> req = web.JsonUtil.parseObject(body);
        String date = web.JsonUtil.getString(req, "date", "").trim();

        if (date.isEmpty() || date.length() < 10) {
            sendResponse(exchange, 400, ApiResult.error("Valid date (YYYY-MM-DD) is required."));
            return;
        }

        boolean success = appointmentDAO.updateAppointmentDate(appointmentId, date);
        if (success) {
            sendResponse(exchange, 200, ApiResult.ok("Appointment date updated", null));
        } else {
            sendResponse(exchange, 500, ApiResult.error("Failed to update appointment."));
        }
    }

    public static void delete(HttpExchange exchange, int appointmentId) throws IOException {
        if (!checkRole(exchange, "Admin")) return;
        boolean success = appointmentDAO.deleteAppointment(appointmentId);
        if (success) {
            sendResponse(exchange, 200, ApiResult.ok("Appointment deleted", null));
        } else {
            sendResponse(exchange, 500, ApiResult.error("Failed to delete appointment."));
        }
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

    static List<Map<String, Object>> appointmentsToListMap(List<Model.Appointment> list) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Model.Appointment a : list) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("appointmentId", a.getAppointmentId());
            map.put("patientName", a.getPatientName());
            map.put("contactNo", a.getContactNo());
            map.put("doctorId", a.getDoctorId());
            map.put("appointmentDate", a.getAppointmentDate());
            map.put("visitType", a.getVisitType());
            map.put("treatmentPrescribed", a.getTreatmentPrescribed());
            map.put("fee", a.getFee());
            map.put("status", a.getStatus());
            result.add(map);
        }
        return result;
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
