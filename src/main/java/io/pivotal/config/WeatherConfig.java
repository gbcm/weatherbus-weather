package io.pivotal.config;

import io.pivotal.Constants;
import io.pivotal.service.IForecastService;
import io.pivotal.service.IWundergroundService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

@Configuration
@Profile({ "default", "cloud" })
public class WeatherConfig {

    @Value("${wunderground.endpoint}")
    private String wundergroudEndpoint;

    @Value("${wunderground.api_key}")
    private String wundergroudApiKey;

    @Value("${forecast.endpoint}")
    private String forecastEndpoint;

    @Value("${forecast.api_key}")
    private String forecastApiKey;

    @Bean
    public IWundergroundService getWundergroundService() {
        String endpoint = String.format("%s/api/%s", wundergroudEndpoint, wundergroudApiKey);
        RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(endpoint);
        builder.setClient(new OkClient());
        RestAdapter adapter = builder.build();
        return adapter.create(IWundergroundService.class);
    }

    @Bean
    public IForecastService getForecastService() {
        String endpoint = String.format("%s/%s", forecastEndpoint, forecastApiKey);
        RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(endpoint);
        builder.setClient(new OkClient());
        RestAdapter adapter = builder.build();
        return adapter.create(IForecastService.class);
    }
}
