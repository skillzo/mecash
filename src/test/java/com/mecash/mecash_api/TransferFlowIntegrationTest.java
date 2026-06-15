package com.mecash.mecash_api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransferFlowIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void signupLoginTransfer() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String signupBody = """
                {
                  "email": "flow-%s@example.com",
                  "username": "flowuser%s",
                  "password": "password123"
                }
                """.formatted(suffix, suffix);

        MvcResult signupResult = mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.account.balance").value(1000))
                .andReturn();

        JsonNode signupJson = objectMapper.readTree(signupResult.getResponse().getContentAsString());
        String accountNumber = signupJson.get("account").get("accountNumber").asText();
        String username = signupJson.get("user").get("username").asText();

        String loginBody = """
                {
                  "username": "%s",
                  "password": "password123"
                }
                """.formatted(username);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .get("token")
                .asText();

        String transferBody = """
                {
                  "fromAccountNumber": "%s",
                  "toAccountNumber": "6574839201",
                  "amount": 25
                }
                """.formatted(accountNumber);

        mockMvc.perform(post("/api/transfer")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transferBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.amountSent").value(25));

        assertThat(accountNumber).hasSize(10);
    }
}
