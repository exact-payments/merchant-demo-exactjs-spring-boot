package com.exactpay.demo;

import com.exactpay.demo.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReCaptchaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void testVerifyRecaptchaSuccess() throws Exception {
        // Create a mock Google reCAPTCHA response
        Map<String, Object> googleResponse = new HashMap<>();
        googleResponse.put("success", true);
        googleResponse.put("score", 0.9);
        googleResponse.put("action", "submit");
        googleResponse.put("challenge_ts", "2023-01-01T12:00:00Z");
        googleResponse.put("hostname", "localhost");

        // Convert to JSON
        String googleResponseJson = JsonUtils.toJson(googleResponse);

        // Mock the RestTemplate response
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(googleResponseJson);

        // Create request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("token", "test-token");
        String requestJson = JsonUtils.toJson(requestBody);

        // Perform the test
        mockMvc.perform(post("/verify-recaptcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.score").value(0.9));
    }

    @Test
    public void testVerifyRecaptchaFailure() throws Exception {
        // Create a mock Google reCAPTCHA response with failure
        Map<String, Object> googleResponse = new HashMap<>();
        googleResponse.put("success", false);
        googleResponse.put("error-codes", new String[]{"invalid-input-response"});

        // Convert to JSON
        String googleResponseJson = JsonUtils.toJson(googleResponse);

        // Mock the RestTemplate response
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(googleResponseJson);

        // Create request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("token", "invalid-token");
        String requestJson = JsonUtils.toJson(requestBody);

        // Perform the test
        mockMvc.perform(post("/verify-recaptcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    public void testVerifyRecaptchaInvalidJson() throws Exception {
        // Mock an invalid JSON response
        String invalidJson = "{\"invalid\":\"json";

        // Mock the RestTemplate response
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(invalidJson);

        // Create request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("token", "test-token");
        String requestJson = JsonUtils.toJson(requestBody);

        // Perform the test
        mockMvc.perform(post("/verify-recaptcha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").exists());
    }
}