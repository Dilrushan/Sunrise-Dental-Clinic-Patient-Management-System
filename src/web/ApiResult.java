package web;

import java.util.LinkedHashMap;
import java.util.Map;

public class ApiResult {
    private final boolean success;
    private final String message;
    private final Object data;

    private ApiResult(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static ApiResult ok(Object data) {
        return new ApiResult(true, "OK", data);
    }

    public static ApiResult ok(String message, Object data) {
        return new ApiResult(true, message, data);
    }

    public static ApiResult error(String message) {
        return new ApiResult(false, message, null);
    }

    public String toJson() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("success", success);
        map.put("message", message);
        map.put("data", data);
        return JsonUtil.toJson(map);
    }
}
