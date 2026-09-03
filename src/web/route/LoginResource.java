package web.route;

import com.sun.net.httpserver.HttpExchange;
import web.ApiResult;
import web.JsonUtil;
import web.SessionManager;
import web.SessionManager.SessionData;
import Controller.LoginController;
import dao.UserDAO;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class LoginResource {

    public static void login(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, ApiResult.error("Method not allowed"));
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), "UTF-8");
        Map<String, Object> req = JsonUtil.parseObject(body);
        String username = JsonUtil.getString(req, "username", "").trim();
        String password = JsonUtil.getString(req, "password", "").trim();

        if (username.isEmpty() || password.isEmpty()) {
            sendResponse(exchange, 400, ApiResult.error("Username and password are required."));
            return;
        }

        LoginController loginController = new LoginController();
        UserDAO.User user = loginController.authenticate(username, password);

        if (user != null) {
            String sessionId = SessionManager.createSession(
                user.getUserId(), user.getUsername(), user.getFullName(), user.getRole());
            SessionManager.setSessionCookie(exchange, sessionId);

            java.util.LinkedHashMap<String, Object> data = new java.util.LinkedHashMap<>();
            data.put("userId", user.getUserId());
            data.put("username", user.getUsername());
            data.put("fullName", user.getFullName());
            data.put("role", user.getRole());
            sendResponse(exchange, 200, ApiResult.ok("Login successful", data));
        } else {
            sendResponse(exchange, 401, ApiResult.error("Invalid username or password."));
        }
    }

    public static void logout(HttpExchange exchange) throws IOException {
        String sessionId = SessionManager.getSessionIdFromExchange(exchange);
        SessionManager.destroySession(sessionId);
        SessionManager.clearSessionCookie(exchange);
        sendResponse(exchange, 200, ApiResult.ok("Logged out", null));
    }

    public static void me(HttpExchange exchange) throws IOException {
        SessionData session = SessionManager.requireAuth(exchange);
        if (session == null) {
            sendResponse(exchange, 401, ApiResult.error("Not authenticated"));
            return;
        }
        java.util.LinkedHashMap<String, Object> data = new java.util.LinkedHashMap<>();
        data.put("userId", session.userId);
        data.put("username", session.username);
        data.put("fullName", session.fullName);
        data.put("role", session.role);
        sendResponse(exchange, 200, ApiResult.ok(data));
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
