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
    public Map<Date, Double> getTemps() {
        Map<Date, Double> temps = new HashMap<>();

        for (HourlyForecast hourlyForecast : hourlyForecasts) {
            temps.put(
                    new Date(Long.parseLong(hourlyForecast.getFctTime().getEpoch())),
                    Double.parseDouble(hourlyForecast.getTemp().getEnglish())
            );
        }

        return temps;
    }

    @SerializedName("hourly_forecast")
    private List<HourlyForecast> hourlyForecasts;

    @Data
    private class HourlyForecast {
        @SerializedName("FCTTIME")
        FctTime fctTime;

        @SerializedName("temp")
        Temp temp;

        @Data
        private class FctTime {
            @SerializedName("epoch")
            String epoch;
        }

        @Data
        private class Temp {
            @SerializedName("english")
            String english;
        }
    }
}
