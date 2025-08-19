package com.example.weatherInfo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WeatherDetailsRequest {
    private int pincode;
    private LocalDate date;
}
