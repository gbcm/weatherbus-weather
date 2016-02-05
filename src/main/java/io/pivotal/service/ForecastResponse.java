package io.pivotal.service;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ForecastResponse {
    public double getCurrentTemperature() {
        return getCurrently().getTemperature();
    }

    public Map<Date, Double> getHourlyTemperatures() {
        Map<Date, Double> temps = new HashMap<>();
        for (Hourly.HourlyData data : getHourly().getData()) {
            temps.put(new Date(data.getTime()), data.getTemperature());
        }

        return temps;
    }

    private Currently currently;

    private Hourly hourly;

    @Data
    public static class Currently {
        private double temperature;
        private String icon;
    }

    @Data
    public static class Hourly {
        private List<HourlyData> data;

        @Data
        public static class HourlyData {
            private long time;
            private double temperature;
            private String icon;
        }
    }
}
