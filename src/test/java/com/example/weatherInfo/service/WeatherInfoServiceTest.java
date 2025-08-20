package com.example.weatherInfo.service;

import com.example.weatherInfo.common.ApplicationProperties;
import com.example.weatherInfo.dto.WeatherDetailsRequest;
import com.example.weatherInfo.dto.downstream.WeatherApiResponse;
import com.example.weatherInfo.entity.WeatherEntity;
import com.example.weatherInfo.repository.WeatherInfoRepository;
import com.example.weatherInfo.utils.WebClientsUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


import java.time.LocalDate;

@SpringBootTest
public class WeatherInfoServiceTest {

    @Mock
    private ApplicationProperties applicationProperties;
    @Mock
    private WebClientsUtils webClientsUtils;

    @Mock
    private WeatherInfoRepository weatherInfoRepository;

    @InjectMocks
    private WeatherInfoService weatherInfoService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        weatherInfoService.objectMapper =objectMapper;
    }

    @Test
    void testGetDetails_WhenDataExistsInDb() throws Exception{

        WeatherDetailsRequest request = new WeatherDetailsRequest();

        request.setPincode(560029);
        request.setDate(LocalDate.of(2025,8,19));

        WeatherApiResponse mockResponse = new WeatherApiResponse();

        WeatherApiResponse.Coord coord = new WeatherApiResponse.Coord();
        coord.setLat(18.5);
        coord.setLon(17.0);
        mockResponse.setCoord(coord);

        String mockJson = objectMapper.writeValueAsString(mockResponse);

        WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.setPincode("560029");
        weatherEntity.setDate("2025-08-19");
        weatherEntity.setWeatherData(mockJson);

        when(weatherInfoRepository.findByPincodeAndDate("560029","2025-08-19"))
                .thenReturn(weatherEntity);

        WeatherApiResponse response = weatherInfoService.getDetails(request);

        assertThat(response).isNotNull();
        assertThat(response.getCoord().getLat()).isEqualTo(18.5);
        assertThat(response.getCoord().getLon()).isEqualTo(17.0);
        verify(weatherInfoRepository,times(1)).findByPincodeAndDate("560029","2025-08-19");
        verifyNoMoreInteractions(webClientsUtils);
    }

    @Test
    void testGetDetails_WhenDataNotExistsInDb() throws JsonProcessingException {
        WeatherDetailsRequest request = new WeatherDetailsRequest();

        request.setPincode(834009);
        request.setDate(LocalDate.of(2025,8,19));

        when(weatherInfoRepository.findByPincodeAndDate("834009","2025-08-19"))
                .thenReturn(null);

        when(applicationProperties.getWeatherBaseUrl()).thenReturn("https://api.openweathermap.org");
        when(applicationProperties.getWeatherInfoPathUrl()).thenReturn("/data/2.5/weather?zip={pincode},IN&appid={key}");
        when(applicationProperties.getWeatherApiKey()).thenReturn("dummy-key");

        WeatherApiResponse response = new WeatherApiResponse();

        WeatherApiResponse.Coord coord = new WeatherApiResponse.Coord();
        coord.setLon(18.0);
        coord.setLat(77.0);
        response.setCoord(coord);

        when(webClientsUtils.get(anyString(),any(HttpHeaders.class),eq(WeatherApiResponse.class))).thenReturn(Mono.just(response));

        WeatherApiResponse apiResponse = weatherInfoService.getDetails(request);

        assertThat(apiResponse).isNotNull();
        assertThat(apiResponse.getCoord().getLon()).isEqualTo(18.0);
        assertThat(apiResponse.getCoord().getLat()).isEqualTo(77.0);

        verify(webClientsUtils,times(1)).get(anyString(),any(HttpHeaders.class),eq(WeatherApiResponse.class));
        verify(weatherInfoRepository,times(1)).save(any(WeatherEntity.class));

    }
}
