package com.kienluu.lab3embed.controller;

import com.kienluu.lab3embed.model.DataResponse;
import com.kienluu.lab3embed.service.MobileAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class MobileAppController {
    private final MobileAppService mobileAppService;

    @GetMapping("/data")
    public ResponseEntity<Object> getData() {
        try {
            DataResponse response = mobileAppService.getDataResponse();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/toggle/{ledId}")
    public ResponseEntity<Object> getToggle(@PathVariable("ledId") Integer ledId) {
        try {
            mobileAppService.toggleButton(ledId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
