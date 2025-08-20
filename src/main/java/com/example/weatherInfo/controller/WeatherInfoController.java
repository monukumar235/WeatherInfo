package com.example.weatherInfo.controller;

import com.example.weatherInfo.dto.WeatherDetailsRequest;
import com.example.weatherInfo.dto.downstream.WeatherApiResponse;
import com.example.weatherInfo.service.WeatherInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherInfoController {
    @Autowired
    WeatherInfoService weatherInfoService;

    @GetMapping("/v1/info")
    public ResponseEntity<WeatherApiResponse> getWeatherDetailsInfo(@RequestBody WeatherDetailsRequest request) throws JsonProcessingException {
        WeatherApiResponse details = weatherInfoService.getDetails(request);
        return ResponseEntity.ok(details);
    }

}
