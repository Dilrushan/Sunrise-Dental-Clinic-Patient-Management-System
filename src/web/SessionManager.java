package web;

import com.sun.net.httpserver.HttpExchange;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private static final String COOKIE_NAME = "SDC_SESSION";
    private static final ConcurrentHashMap<String, SessionData> sessions = new ConcurrentHashMap<>();

    public static class SessionData {
        public final int userId;
        public final String username;
        public final String fullName;
        public final String role;

        public SessionData(int userId, String username, String fullName, String role) {
            this.userId = userId;
            this.username = username;
            this.fullName = fullName;
            this.role = role;
        }
    }

    public static String createSession(int userId, String username, String fullName, String role) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, new SessionData(userId, username, fullName, role));
        return sessionId;
    }

    public static SessionData getSession(String sessionId) {
        if (sessionId == null) return null;
        return sessions.get(sessionId);
    }

    public static void destroySession(String sessionId) {
        if (sessionId != null) {
            sessions.remove(sessionId);
        }
    }

    public static String getSessionIdFromExchange(HttpExchange exchange) {
        String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
        if (cookieHeader == null) return null;
        for (String cookie : cookieHeader.split(";")) {
            cookie = cookie.trim();
            if (cookie.startsWith(COOKIE_NAME + "=")) {
                return cookie.substring(COOKIE_NAME.length() + 1);
            }
        }
        return null;
    }

    public static SessionData requireAuth(HttpExchange exchange) {
        String sessionId = getSessionIdFromExchange(exchange);
        return getSession(sessionId);
    }

    public static void setSessionCookie(HttpExchange exchange, String sessionId) {
        String cookie = COOKIE_NAME + "=" + sessionId
            + "; Path=/; HttpOnly; SameSite=Lax; Max-Age=3600";
        exchange.getResponseHeaders().add("Set-Cookie", cookie);
    }

    public static void clearSessionCookie(HttpExchange exchange) {
        String cookie = COOKIE_NAME + "=; Path=/; HttpOnly; SameSite=Lax; Max-Age=0";
        exchange.getResponseHeaders().add("Set-Cookie", cookie);
    }
}
