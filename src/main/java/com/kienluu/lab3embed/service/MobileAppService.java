package com.kienluu.lab3embed.service;

import com.kienluu.lab3embed.model.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class MobileAppService {
    private final RestTemplate restTemplate;
    private final String ESP_URL = "http://localhost:8180";
    public DataResponse getDataResponse() {
        return restTemplate.getForObject(ESP_URL+"/data", DataResponse.class);
    }

    public void toggleButton(Integer button) {
        restTemplate.getForObject(ESP_URL+"/toggle/"+button, String.class);
    }

    public void sendMessage(Long userId, String message) {
        // Tạo header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Tạo body
        String jsonBody = "{\"message\": \"" + message + "\"}";

        // Gửi request
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        String url = ESP_URL+"/message/" + userId;

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        System.out.println("Response: " + response.getBody());
    }
}
