package web;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ApiResultTest {

    @Test
    public void testOkWithData() {
        ApiResult result = ApiResult.ok("payload");
        String json = result.toJson();
        assertTrue(json.contains("\"success\":true"));
        assertTrue(json.contains("\"message\":\"OK\""));
        assertTrue(json.contains("\"data\":\"payload\""));
    }

    @Test
    public void testOkWithMessageAndData() {
        ApiResult result = ApiResult.ok("Successfully created", 42);
        String json = result.toJson();
        assertTrue(json.contains("\"success\":true"));
        assertTrue(json.contains("\"message\":\"Successfully created\""));
        assertTrue(json.contains("\"data\":42"));
    }

    @Test
    public void testError() {
        ApiResult result = ApiResult.error("Something failed");
        String json = result.toJson();
        assertTrue(json.contains("\"success\":false"));
        assertTrue(json.contains("\"message\":\"Something failed\""));
        assertTrue(json.contains("\"data\":null"));
    }

    @Test
    public void testToJsonIsValidStructure() {
        ApiResult result = ApiResult.ok("data");
        String json = result.toJson();
        assertTrue(json.startsWith("{"));
        assertTrue(json.endsWith("}"));
    }
}
