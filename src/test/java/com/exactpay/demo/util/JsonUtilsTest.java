package com.exactpay.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilsTest {

    // Test class for JSON serialization/deserialization
    static class TestPerson {
        private String name;
        private int age;

        public TestPerson() {
        }

        public TestPerson(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestPerson that = (TestPerson) o;
            return age == that.age && name.equals(that.name);
        }
    }

    @Test
    void getObjectMapper() {
        ObjectMapper mapper = JsonUtils.getObjectMapper();
        assertNotNull(mapper, "ObjectMapper should not be null");
    }

    @Test
    void toJson() throws JsonProcessingException {
        TestPerson person = new TestPerson("John Doe", 30);
        String json = JsonUtils.toJson(person);
        
        assertNotNull(json, "JSON string should not be null");
        assertTrue(json.contains("\"name\":\"John Doe\""), "JSON should contain name");
        assertTrue(json.contains("\"age\":30"), "JSON should contain age");
    }

    @Test
    void fromJson() throws IOException {
        String json = "{\"name\":\"Jane Doe\",\"age\":25}";
        TestPerson person = JsonUtils.fromJson(json, TestPerson.class);
        
        assertNotNull(person, "Deserialized object should not be null");
        assertEquals("Jane Doe", person.getName(), "Name should match");
        assertEquals(25, person.getAge(), "Age should match");
    }

    @Test
    void fromJsonWithTypeReference() throws IOException {
        String json = "{\"name\":\"Bob Smith\",\"age\":40}";
        TestPerson person = JsonUtils.fromJson(json, new TypeReference<TestPerson>() {});
        
        assertNotNull(person, "Deserialized object should not be null");
        assertEquals("Bob Smith", person.getName(), "Name should match");
        assertEquals(40, person.getAge(), "Age should match");
    }

    @Test
    void toMap() {
        TestPerson person = new TestPerson("Alice Johnson", 35);
        Map<String, Object> map = JsonUtils.toMap(person);
        
        assertNotNull(map, "Map should not be null");
        assertEquals("Alice Johnson", map.get("name"), "Name should match");
        assertEquals(35, map.get("age"), "Age should match");
    }

    @Test
    void fromMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Charlie Brown");
        map.put("age", 50);
        
        TestPerson person = JsonUtils.fromMap(map, TestPerson.class);
        
        assertNotNull(person, "Converted object should not be null");
        assertEquals("Charlie Brown", person.getName(), "Name should match");
        assertEquals(50, person.getAge(), "Age should match");
    }

    @Test
    void handleComplexJson() throws IOException {
        // Test with nested objects and arrays
        String complexJson = "{\"person\":{\"name\":\"David Miller\",\"age\":45},\"tags\":[\"developer\",\"java\"]}";
        
        Map<String, Object> result = JsonUtils.fromJson(complexJson, new TypeReference<Map<String, Object>>() {});
        
        assertNotNull(result, "Result should not be null");
        assertTrue(result.containsKey("person"), "Result should contain person key");
        assertTrue(result.containsKey("tags"), "Result should contain tags key");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> personMap = (Map<String, Object>) result.get("person");
        assertEquals("David Miller", personMap.get("name"), "Name should match");
        assertEquals(45, personMap.get("age"), "Age should match");
    }
}