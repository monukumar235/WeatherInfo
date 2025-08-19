package com.example.weatherInfo.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class ApplicationProperties {

    @Value("${weather.base.url}")
    public  String weatherBaseUrl;

    @Value("${weather.info.url}")
    public  String weatherInfoPathUrl;
    @Value("${weather.api.key}")
    private String weatherApiKey;

}
