package io.pivotal.config;

import io.pivotal.Constants;
import io.pivotal.service.IForecastService;
import io.pivotal.service.IWundergroundService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

@Configuration
@Profile({ "default", "cloud" })
public class WeatherConfig {
    public static final String WUNDERGROUND_ENDPOINT = System.getenv("WUNDERGROUND_ENDPOINT");
    public static final String FORECAST_ENDPOINT = System.getenv("FORECAST_ENDPOINT");
    public static final String WUNDERGROUND_API_KEY = System.getenv("WUNDERGROUND_API_KEY");
    public static final String FORECAST_API_KEY = System.getenv("FORECAST_API_KEY");

    @Bean
    public IWundergroundService getWundergroundService() {
        String endpoint = String.format("%s/api/%s", WUNDERGROUND_ENDPOINT, WUNDERGROUND_API_KEY);
        RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(endpoint);
        builder.setClient(new OkClient());
        RestAdapter adapter = builder.build();
        return adapter.create(IWundergroundService.class);
    }

    @Bean
    public IForecastService getForecastService() {
        String endpoint = String.format("%s/%s", FORECAST_ENDPOINT, FORECAST_API_KEY);
        RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(endpoint);
        builder.setClient(new OkClient());
        RestAdapter adapter = builder.build();
        return adapter.create(IForecastService.class);
    }
}
