package com.kienluu.lab3embed.controller;

import com.kienluu.lab3embed.config.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/control")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ControlController {

    private final WebSocketHandler webSocketHandler;

    @PostMapping("/control")
    public String sendCommand(@RequestParam String command) {
        webSocketHandler.sendMessageToClients(command);
        return "Command sent: " + command;
    }

    @PostMapping("/data")
    public ResponseEntity<Object> sendData(@RequestParam Map<String, Object> data) {
        try{
            log.info(data.toString());
            return ResponseEntity.ok(data);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/data")
    public ResponseEntity<Object> getData() {
        try {
            return ResponseEntity.ok(1);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

