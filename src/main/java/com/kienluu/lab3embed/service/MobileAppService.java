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

    public Boolean toggleButton(Integer button) {
        try {
            // Gửi request và nhận response dạng String
            ResponseEntity<String> response = restTemplate.getForEntity(ESP_URL + "/toggle/" + button, String.class);

            if (response.getBody() != null) {
                // Chuyển đổi chuỗi thành Boolean
                return Boolean.parseBoolean(response.getBody().trim());
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean setAutoMode(Boolean autoMode) {
        try {
            // Gửi request và nhận response dạng String
            ResponseEntity<String> response = restTemplate.getForEntity(ESP_URL + "/auto", String.class);

            if (response.getBody() != null) {
                // Chuyển đổi chuỗi thành Boolean
                return Boolean.parseBoolean(response.getBody().trim());
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean toggleLed3(){
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(ESP_URL + "/led3", String.class);

            if (response.getBody() != null) {
                return Boolean.parseBoolean(response.getBody().trim());
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateThreshold(Integer temperature, Integer humidity) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        String jsonBody = "{\"temperature\":" + temperature + ", \"humidity\":" + humidity + "}";
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        restTemplate.postForObject(ESP_URL + "/threshold", requestEntity, String.class);
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
