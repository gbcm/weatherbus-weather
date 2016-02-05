package io.pivotal.service;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Created by pivotal on 1/6/16.
 */
@Data
public class WeatherConditionsResponse {
    public double getTempF() {
        return this.getCurrentObservation().getTempF();
    }
    public String getIcon() {
        return this.getCurrentObservation().getIcon();
    }

    @Data
    public static class CurrentObservation {
        @SerializedName("temp_f")
        private double tempF;
        private String icon;
    }

    @SerializedName("current_observation")
    private CurrentObservation currentObservation;
}
