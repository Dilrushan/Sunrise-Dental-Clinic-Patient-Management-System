package web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import web.handler.StaticFileHandler;
import web.route.LoginResource;
import web.route.AdminResource;
import web.route.DoctorResource;
import web.route.ReceptionistResource;
import web.route.AppointmentResource;
import web.route.PatientResource;
import web.route.StaffResource;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebServer {

    private static final int PORT = 8080;
    private static final List<Route> routes = new ArrayList<>();
    private static StaticFileHandler staticHandler;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        staticHandler = new StaticFileHandler("web" + File.separator + "static");

        route("POST", "/api/login", (ex, p) -> LoginResource.login(ex));
        route("POST", "/api/logout", (ex, p) -> LoginResource.logout(ex));
        route("GET", "/api/me", (ex, p) -> LoginResource.me(ex));

        route("GET", "/api/appointments", (ex, p) -> {
            var session = SessionManager.requireAuth(ex);
            if (session == null) { sendError(ex, 401, "Not authenticated"); return; }
            if ("Admin".equals(session.role)) {
                AdminResource.getAll(ex);
            } else if ("Receptionist".equals(session.role)) {
                ReceptionistResource.getAll(ex);
            } else {
                sendError(ex, 403, "Access denied");
            }
        });

        route("GET", "/api/appointments/search", (ex, p) -> {
            var session = SessionManager.requireAuth(ex);
            if (session == null) { sendError(ex, 401, "Not authenticated"); return; }
            if ("Admin".equals(session.role)) {
                AdminResource.search(ex);
            } else if ("Receptionist".equals(session.role)) {
                ReceptionistResource.search(ex);
            } else {
                sendError(ex, 403, "Access denied");
            }
        });

        route("GET", "/api/appointments/doctor", (ex, p) -> DoctorResource.getDoctorAppointments(ex));
        route("PUT", "/api/appointments/:id/prescription", (ex, p) -> DoctorResource.savePrescription(ex, getIntParam(p, "id")));
        route("PUT", "/api/appointments/:id/date", (ex, p) -> AdminResource.updateDate(ex, getIntParam(p, "id")));
        route("PUT", "/api/appointments/:id/visit-type", (ex, p) -> ReceptionistResource.updateVisitType(ex, getIntParam(p, "id")));
        route("DELETE", "/api/appointments/:id", (ex, p) -> AdminResource.delete(ex, getIntParam(p, "id")));

        route("POST", "/api/appointments", (ex, p) -> AppointmentResource.bookExisting(ex));
        route("POST", "/api/appointments/new-patient", (ex, p) -> AppointmentResource.registerAndBook(ex));

        route("GET", "/api/doctors", (ex, p) -> AppointmentResource.listDoctors(ex));
        route("GET", "/api/patients/list", (ex, p) -> AppointmentResource.listPatients(ex));
        route("GET", "/api/treatments", (ex, p) -> AppointmentResource.listTreatments(ex));

        route("POST", "/api/patients", (ex, p) -> PatientResource.register(ex));
        route("POST", "/api/staff", (ex, p) -> StaffResource.register(ex));
        route("POST", "/api/billing/calculate", (ex, p) -> ReceptionistResource.calculateBill(ex));

        server.createContext("/", WebServer::handleRequest);
        server.setExecutor(null);
        server.start();
        System.out.println("Sunrise Dental Clinic Web Server started on http://localhost:" + PORT);
    }

    private static int getIntParam(Map<String, String> params, String name) {
        try {
            return Integer.parseInt(params.getOrDefault(name, "-1"));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void route(String method, String path, ResourceHandler handler) {
        String regex = path.replaceAll(":([a-zA-Z]+)", "(?<$1>[^/]+)");
        Pattern pattern = Pattern.compile("^" + regex + "$");
        routes.add(new Route(method, pattern, handler));
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (path.startsWith("/api/")) {
            for (Route r : routes) {
                if (r.method.equalsIgnoreCase(method)) {
                    Matcher m = r.pattern.matcher(path);
                    if (m.matches()) {
                        Map<String, String> params = new LinkedHashMap<>();
                        java.util.regex.Matcher nm = Pattern.compile("\\(\\?<([a-zA-Z]+)>").matcher(r.pattern.pattern());
                        while (nm.find()) {
                            String name = nm.group(1);
                            try { params.put(name, m.group(name)); } catch (Exception ignored) {}
                        }
                        r.handler.handle(exchange, params);
                        return;
                    }
                }
            }
            sendError(exchange, 404, "API endpoint not found: " + method + " " + path);
        } else {
            staticHandler.handle(exchange);
        }
    }

    private static void sendError(HttpExchange exchange, int status, String message) throws IOException {
        ApiResult result = ApiResult.error(message);
        byte[] bytes = result.toJson().getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    @FunctionalInterface
    interface ResourceHandler {
        void handle(HttpExchange exchange, Map<String, String> params) throws IOException;
    }

    static class Route {
        final String method;
        final Pattern pattern;
        final ResourceHandler handler;

        Route(String method, Pattern pattern, ResourceHandler handler) {
            this.method = method;
            this.pattern = pattern;
            this.handler = handler;
        }
    }
}
