package io.pivotal.service;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pivotal on 1/7/16.
 */
@Data
public class WeatherForecastResponse {
    @SerializedName("hourly_forecast")
    private List<HourlyForecast> hourlyForecasts;

    @Data
    public static class HourlyForecast {
        @SerializedName("FCTTIME")
        FctTime fctTime;

        @SerializedName("temp")
        Temp temp;

        @SerializedName("icon")
        String wundergroundIcon;

        @Data
        public static class FctTime {
            @SerializedName("epoch")
            String epoch;
        }

        @Data
        public static class Temp {
            @SerializedName("english")
            String english;
        }
    }
}
