package web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilTest {

    @Test
    public void testToJsonNull() {
        assertEquals("null", JsonUtil.toJson(null));
    }

    @Test
    public void testToJsonString() {
        assertEquals("\"hello\"", JsonUtil.toJson("hello"));
        assertEquals("\"a\\\"b\"", JsonUtil.toJson("a\"b"));
    }

    @Test
    public void testToJsonNumber() {
        assertEquals("42", JsonUtil.toJson(42));
        assertEquals("3.14", JsonUtil.toJson(3.14));
    }

    @Test
    public void testToJsonBoolean() {
        assertEquals("true", JsonUtil.toJson(true));
        assertEquals("false", JsonUtil.toJson(false));
    }

    @Test
    public void testToJsonMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", "John");
        map.put("age", 30);
        String json = JsonUtil.toJson(map);
        assertTrue(json.contains("\"name\":\"John\""));
        assertTrue(json.contains("\"age\":30"));
        assertTrue(json.startsWith("{"));
        assertTrue(json.endsWith("}"));
    }

    @Test
    public void testToJsonList() {
        List<Object> list = new ArrayList<>();
        list.add(1);
        list.add("two");
        list.add(true);
        String json = JsonUtil.toJson(list);
        assertEquals("[1,\"two\",true]", json);
    }

    @Test
    public void testParseObject() {
        Map<String, Object> map = JsonUtil.parseObject("{\"name\":\"John\",\"age\":30}");
        assertEquals("John", map.get("name"));
        assertEquals(30, map.get("age"));
    }

    @Test
    public void testParseArray() {
        List<Object> list = JsonUtil.parseArray("[1,\"two\",true]");
        assertEquals(1, list.get(0));
        assertEquals("two", list.get(1));
        assertEquals(true, list.get(2));
    }

    @Test
    public void testGetStringFromMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", "John");
        assertEquals("John", JsonUtil.getString(map, "name", "default"));
        assertEquals("default", JsonUtil.getString(map, "missing", "default"));
    }

    @Test
    public void testGetIntFromMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("age", 30);
        assertEquals(30, JsonUtil.getInt(map, "age", 0));
        assertEquals(5, JsonUtil.getInt(map, "missing", 5));
    }

    @Test
    public void testGetDoubleFromMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("fee", 1050.50);
        assertEquals(1050.50, JsonUtil.getDouble(map, "fee", 0.0), 0.001);
        assertEquals(9.9, JsonUtil.getDouble(map, "missing", 9.9), 0.001);
    }
}
