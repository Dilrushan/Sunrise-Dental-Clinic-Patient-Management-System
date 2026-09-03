package web.route;

import com.sun.net.httpserver.HttpExchange;
import web.ApiResult;
import web.JsonUtil;
import web.SessionManager;
import web.SessionManager.SessionData;
import Controller.StaffController;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class StaffResource {

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
        if (!"Admin".equals(session.role)) {
            sendResponse(exchange, 403, ApiResult.error("Only admins can register staff."));
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");
        Map<String, Object> req = JsonUtil.parseObject(body);
        String username = JsonUtil.getString(req, "username", "").trim();
        String password = JsonUtil.getString(req, "password", "");
        String confirmPassword = JsonUtil.getString(req, "confirmPassword", "");
        String fullName = JsonUtil.getString(req, "fullName", "").trim();
        String role = JsonUtil.getString(req, "role", "").trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            sendResponse(exchange, 400, ApiResult.error("Username and password cannot be empty."));
            return;
        }

        if (username.length() < 5) {
            sendResponse(exchange, 400, ApiResult.error("Username must be at least 5 characters long."));
            return;
        }

        if (password.length() < 8) {
            sendResponse(exchange, 400, ApiResult.error("Password must be at least 8 characters long."));
            return;
        }

        if (!password.equals(confirmPassword)) {
            sendResponse(exchange, 400, ApiResult.error("Passwords do not match!"));
            return;
        }

        if (fullName.isEmpty()) fullName = username;

        StaffController staffController = new StaffController();
        String result = staffController.registerStaff(username, password, confirmPassword, fullName, role);

        if ("SUCCESS".equals(result)) {
            sendResponse(exchange, 200, ApiResult.ok("Staff user '" + username + "' registered with role: " + role, null));
        } else {
            sendResponse(exchange, 400, ApiResult.error(result));
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
