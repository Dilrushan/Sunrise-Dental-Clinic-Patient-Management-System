package web.route;

import com.sun.net.httpserver.HttpExchange;
import web.ApiResult;
import web.JsonUtil;
import web.NotificationService;
import web.SessionManager;
import web.SessionManager.SessionData;
import Controller.AppointmentController;
import dao.AppointmentDAO;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppointmentResource {

    private static final AppointmentController appointmentController = new AppointmentController();
    private static final AppointmentDAO appointmentDAO = new AppointmentDAO();

    public static void bookExisting(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, ApiResult.error("Method not allowed"));
            return;
        }
        SessionData session = SessionManager.requireAuth(exchange);
        if (session == null) {
            sendResponse(exchange, 401, ApiResult.error("Not authenticated"));
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");
        Map<String, Object> req = JsonUtil.parseObject(body);
        String patientName = JsonUtil.getString(req, "patientName", "").trim();
        String contact = JsonUtil.getString(req, "contact", "").trim();
        int doctorId = JsonUtil.getInt(req, "doctorId", -1);
        String date = JsonUtil.getString(req, "date", "").trim();
        String visitType = JsonUtil.getString(req, "visitType", "").trim();

        if (patientName.isEmpty() || contact.isEmpty() || doctorId <= 0 || date.isEmpty()) {
            sendResponse(exchange, 400, ApiResult.error("All fields are required."));
            return;
        }

        if (appointmentController.isDuplicateBooking(patientName, doctorId, date)) {
            sendResponse(exchange, 409, ApiResult.error("Duplicate booking: patient already has an appointment with this doctor on " + date + "."));
            return;
        }

        boolean success = appointmentController.bookAppointmentForExistingPatient(
            patientName, contact, doctorId, date, visitType);
        if (success) {
            String notification = NotificationService.getLastEmailContent();
            java.util.LinkedHashMap<String, Object> data = new java.util.LinkedHashMap<>();
            data.put("notification", notification);
            sendResponse(exchange, 200, ApiResult.ok("Appointment booked successfully", data));
        } else {
            sendResponse(exchange, 500, ApiResult.error("Failed to book appointment."));
        }
    }

    public static void registerAndBook(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, ApiResult.error("Method not allowed"));
            return;
        }
        SessionData session = SessionManager.requireAuth(exchange);
        if (session == null) {
            sendResponse(exchange, 401, ApiResult.error("Not authenticated"));
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");
        Map<String, Object> req = JsonUtil.parseObject(body);
        String name = JsonUtil.getString(req, "name", "").trim();
        String address = JsonUtil.getString(req, "address", "").trim();
        String contact = JsonUtil.getString(req, "contact", "").trim();
        String email = JsonUtil.getString(req, "email", "").trim();
        String history = JsonUtil.getString(req, "history", "").trim();
        int doctorId = JsonUtil.getInt(req, "doctorId", 1);
        String date = JsonUtil.getString(req, "date", "").trim();
        String visitType = JsonUtil.getString(req, "visitType", "").trim();

        if (name.isEmpty() || contact.isEmpty()) {
            sendResponse(exchange, 400, ApiResult.error("Patient name and contact are required."));
            return;
        }

        if (name.length() < 5) {
            sendResponse(exchange, 400, ApiResult.error("Patient name must be at least 5 characters."));
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            sendResponse(exchange, 400, ApiResult.error("Please enter a valid email address."));
            return;
        }

        if (new dao.PatientDAO().isEmailExists(email)) {
            sendResponse(exchange, 409, ApiResult.error("An account with this email already exists."));
            return;
        }

        if (date.isEmpty() || date.length() < 10) {
            sendResponse(exchange, 400, ApiResult.error("Please enter a valid date (YYYY-MM-DD)."));
            return;
        }

        boolean success = appointmentController.addNewPatientAndAppointment(
            name, address, contact, email, history, doctorId, date, visitType);
        if (success) {
            String notification = NotificationService.getLastEmailContent();
            java.util.LinkedHashMap<String, Object> data = new java.util.LinkedHashMap<>();
            data.put("notification", notification);
            sendResponse(exchange, 200, ApiResult.ok("Patient and appointment registered successfully", data));
        } else {
            sendResponse(exchange, 500, ApiResult.error("Failed to register. Check constraints or duplicate booking."));
        }
    }

    public static void listDoctors(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, ApiResult.error("Method not allowed"));
            return;
        }
        List<String[]> doctors = appointmentDAO.getAllDoctors();
        List<Map<String, Object>> result = new ArrayList<>();
        for (String[] d : doctors) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("userId", Integer.parseInt(d[0]));
            map.put("fullName", d[1]);
            result.add(map);
        }
        sendResponse(exchange, 200, ApiResult.ok(result));
    }

    public static void listPatients(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, ApiResult.error("Method not allowed"));
            return;
        }
        List<String[]> patients = appointmentDAO.getAllPatients();
        List<Map<String, Object>> result = new ArrayList<>();
        for (String[] p : patients) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("fullName", p[0]);
            map.put("contactNumber", p[1]);
            result.add(map);
        }
        sendResponse(exchange, 200, ApiResult.ok(result));
    }

    public static void listTreatments(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, ApiResult.error("Method not allowed"));
            return;
        }
        List<String[]> treatments = appointmentDAO.getTreatments();
        List<Map<String, Object>> result = new ArrayList<>();
        for (String[] t : treatments) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("treatmentName", t[0]);
            map.put("fee", Double.parseDouble(t[1]));
            result.add(map);
        }
        sendResponse(exchange, 200, ApiResult.ok(result));
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
