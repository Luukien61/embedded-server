package com.kienluu.lab3embed.controller;

import com.kienluu.lab3embed.model.DataResponse;
import com.kienluu.lab3embed.model.Threshold;
import com.kienluu.lab3embed.service.MobileAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
            Boolean state = mobileAppService.toggleButton(ledId);
            return ResponseEntity.ok(state);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/led3")
    public ResponseEntity<Object> getLed3() {
        try{
            Boolean state = mobileAppService.toggleLed3();
            return ResponseEntity.ok().body(state);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/auto")
    public ResponseEntity<Object> getAuto() {
        try{
            Boolean state = mobileAppService.setAutoMode(Boolean.TRUE);
            return ResponseEntity.ok().body(state);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/threshold")
    public ResponseEntity<Object> threshold(@RequestBody Threshold body) {
        try{
            mobileAppService.updateThreshold(body.getTemperature(), body.getHumidity());
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

}
