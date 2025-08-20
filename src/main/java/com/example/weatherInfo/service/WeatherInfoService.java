package com.example.weatherInfo.service;

import com.example.weatherInfo.common.ApplicationProperties;
import com.example.weatherInfo.dto.WeatherDetailsRequest;
import com.example.weatherInfo.dto.downstream.WeatherApiResponse;
import com.example.weatherInfo.entity.WeatherEntity;
import com.example.weatherInfo.repository.WeatherInfoRepository;
import com.example.weatherInfo.utils.WebClientsUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class WeatherInfoService {
    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private WebClientsUtils webClientsUtils;

    @Autowired
    private WeatherInfoRepository weatherInfoRepository;
    @Autowired
    ObjectMapper objectMapper;


    public WeatherApiResponse getDetails(WeatherDetailsRequest weatherDetailsRequest) throws JsonProcessingException {


        WeatherEntity weatherInfoRepositoryByPinCodeAndDate = weatherInfoRepository.findByPincodeAndDate(String.valueOf(weatherDetailsRequest.getPincode()), weatherDetailsRequest.getDate().toString());

        if(weatherInfoRepositoryByPinCodeAndDate!=null){
            WeatherApiResponse weatherApiResponse = objectMapper.readValue(weatherInfoRepositoryByPinCodeAndDate.getWeatherData(), WeatherApiResponse.class);
            return weatherApiResponse;
        }

        String url = getUrl(applicationProperties,weatherDetailsRequest);

        WeatherApiResponse weatherApiResponse = webClientsUtils.get(url, new HttpHeaders(), WeatherApiResponse.class).block();

        double lat = weatherApiResponse.getCoord().getLat();
        double lon = weatherApiResponse.getCoord().getLon();

        WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.setLat((int)lat);
        weatherEntity.setLon((int)lon);
        weatherEntity.setPincode(String.valueOf(weatherDetailsRequest.getPincode()));
        weatherEntity.setDate(weatherDetailsRequest.getDate().toString());

        weatherEntity.setWeatherData(objectMapper.writeValueAsString(weatherApiResponse));
        weatherInfoRepository.save(weatherEntity);


        return weatherApiResponse;
    }

    private String getUrl(ApplicationProperties applicationProperties,WeatherDetailsRequest request) {
        String url = applicationProperties.getWeatherBaseUrl() + applicationProperties.getWeatherInfoPathUrl().replace("{pincode}",String.valueOf(request.getPincode())).replace("{key}",applicationProperties.getWeatherApiKey());
        return url;

    }
}
