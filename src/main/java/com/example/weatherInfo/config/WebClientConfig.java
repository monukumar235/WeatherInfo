package com.example.weatherInfo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient getWebClient(WebClient.Builder webClientBuilder) {
        HttpClient httpClient = HttpClient.create();
        ClientHttpConnector conn = new ReactorClientHttpConnector(httpClient);
        return webClientBuilder
                .clientConnector(conn)
//                .filter(new RequestLoggingFilterFunction())
                .build();
    }
}
