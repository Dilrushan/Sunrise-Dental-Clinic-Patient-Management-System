package web.route;

import com.sun.net.httpserver.HttpExchange;
import web.ApiResult;
import web.JsonUtil;
import web.SessionManager;
import web.SessionManager.SessionData;
import dao.PatientDAO;
import Model.Patient;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class PatientResource {

    private static final PatientDAO patientDAO = new PatientDAO();

    public static void register(HttpExchange exchange) throws IOException {
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
        String fullName = JsonUtil.getString(req, "fullName", "").trim();
        String address = JsonUtil.getString(req, "address", "").trim();
        String contactNumber = JsonUtil.getString(req, "contactNumber", "").trim();
        String email = JsonUtil.getString(req, "email", "").trim();
        String treatmentHistory = JsonUtil.getString(req, "treatmentHistory", "").trim();

        if (fullName.isEmpty() || contactNumber.isEmpty() || email.isEmpty()) {
            sendResponse(exchange, 400, ApiResult.error("Full name, contact number, and email are required."));
            return;
        }

        if (patientDAO.isEmailExists(email)) {
            sendResponse(exchange, 409, ApiResult.error("An account with this email already exists."));
            return;
        }

        Patient patient = new Patient(0, fullName, address, contactNumber, email, treatmentHistory);
        int id = patientDAO.registerPatient(patient);
        if (id > 0) {
            java.util.LinkedHashMap<String, Object> data = new java.util.LinkedHashMap<>();
            data.put("patientId", id);
            sendResponse(exchange, 200, ApiResult.ok("Patient registered successfully", data));
        } else {
            sendResponse(exchange, 500, ApiResult.error("Failed to register patient."));
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
