package com.example.weatherInfo.repository;

import com.example.weatherInfo.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherInfoRepository extends JpaRepository<WeatherEntity,Integer> {
    WeatherEntity findByPincodeAndDate(String pincode, String toString);
}
