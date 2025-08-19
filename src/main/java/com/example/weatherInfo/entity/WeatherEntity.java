package com.example.weatherInfo.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "weather")
public class WeatherEntity {

    @Id
    @Column(name = "weather_id",nullable = false)
    private int id;
    @Column(name = "lat",nullable = false)
    private int lat;

    @Column(name = "lon")
    private int lon;
    @Column(name = "pin_code")
    private String pincode;
    @Column(columnDefinition = "JSON",name = "weather_map",nullable = true)
    private String weatherData;
    @Column(name = "date")
    private String date;
}
