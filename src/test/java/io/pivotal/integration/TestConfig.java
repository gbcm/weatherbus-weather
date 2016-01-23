package io.pivotal.integration;

import io.pivotal.service.IWundergroundService;
import io.pivotal.service.WeatherConditionsResponse;
import io.pivotal.service.WeatherConditionsResponseBuilder;
import io.pivotal.service.WeatherForecastResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import retrofit.http.Path;

@Configuration
@Profile("test")
public class TestConfig {
    @Bean
    public IWundergroundService getWundergroundService() {
        return new IWundergroundService() {
            @Override
            public WeatherConditionsResponse getConditionsResponse(@Path("latitude") String latitude, @Path("longitude") String longitude) {
                return new WeatherConditionsResponseBuilder().build();
            }

            @Override
            public WeatherForecastResponse getForecastResponse(@Path("latitude") String latitude, @Path("longitude") String longitude) {
                return null;
            }
        };
    }
}
