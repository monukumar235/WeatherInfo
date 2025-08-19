package com.example.weatherInfo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class WebClientsUtils {


    @Autowired
    private  WebClient webClient;



    public <T> Mono<T> get(String url, HttpHeaders headers, Class<T>responseType){
        return webClient
                .get()
                .uri(url)
                .headers(httpHeaders->{
                    if(headers!=null && !headers.isEmpty()){
                        httpHeaders.addAll(headers);
                    }
                })
                .retrieve()
                .bodyToMono(responseType);
    }
}
