package com.exactpay.demo;

import com.exactpay.demo.util.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void testCheckoutWithDecimalAmount() throws Exception {
        // Create a mock Order response
        Order mockOrder = new Order();
        AccessToken accessToken = new AccessToken();
        accessToken.setToken("test-token");
        mockOrder.setAccessToken(accessToken);
        mockOrder.setOrderId("test-order-id");
        
        // Convert the mock order to JSON string
        String orderJson = JsonUtils.toJson(mockOrder);
        
        // Mock the RestTemplate response with String return type (JSON)
        ResponseEntity<String> mockResponse = new ResponseEntity<>(orderJson, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(mockResponse);
        
        // Test with a decimal amount (1050 cents = $10.50)
        mockMvc.perform(MockMvcRequestBuilders.get("/checkout")
                .param("amountStr", "1050"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("amount", "$10.5"));
        
        // Test with another decimal amount (99 cents = $0.99)
        mockMvc.perform(MockMvcRequestBuilders.get("/checkout")
                .param("amountStr", "99"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("amount", "$0.99"));
    }
    
    @Test
    public void testCheckoutWithErrorHandling() throws Exception {
        // Mock an invalid JSON response to test error handling
        String invalidJson = "{\"invalid\":\"json";
        
        // Mock the RestTemplate response with invalid JSON
        ResponseEntity<String> mockResponse = new ResponseEntity<>(invalidJson, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(mockResponse);
        
        // Test that the controller properly handles JSON parsing errors
        mockMvc.perform(MockMvcRequestBuilders.get("/checkout")
                .param("amountStr", "1000"))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }
}