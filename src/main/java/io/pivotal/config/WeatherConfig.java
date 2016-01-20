package io.pivotal.config;

import io.pivotal.Constants;
import io.pivotal.service.IWundergroundService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

@Configuration
@Profile({ "default", "cloud" })
public class WeatherConfig {
    @Bean
    public IWundergroundService getWundergroundService() {
        RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(Constants.WUNDERGROUND_ENDPOINT);

        builder.setClient(new OkClient());
        RestAdapter adapter = builder.build();
        return adapter.create(IWundergroundService.class);
    }
}
