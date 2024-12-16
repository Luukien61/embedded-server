package com.kienluu.lab3embed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataResponse {
    private Float temperature;
    private Float humidity;
    private Boolean button1;
    private Boolean button2;
    private Boolean button3;
    private Boolean isAutoMode;
    private Integer temperatureThreshold;
    private Integer humidityThreshold;
}
//"temperature":24,"humidity":40,"button1":false,"button2":false