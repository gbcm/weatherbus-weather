package io.pivotal.controller;

import io.pivotal.errorHandling.ErrorController;
import io.pivotal.model.Coordinate;
import io.pivotal.service.WeatherService;
import io.pivotal.view.ForecastPresenter;
import io.pivotal.view.TemperaturePresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by pivotal on 1/4/16.
 */
@Controller
public class WeatherController {
    @Autowired
    WeatherService weatherService;

    @RequestMapping("/")
    public @ResponseBody String getCurrentTemp(@RequestParam double lat, @RequestParam double lng) throws Exception {
        if (!isValidLatAndLng(lat, lng)) {
            throw new IllegalArgumentException("Bad query params to '/' ");
        }
        return new TemperaturePresenter(lat, lng,
                weatherService.getCurrentTemp(new Coordinate(lat, lng))).toJson();
    }


    @RequestMapping("/forecast")
    public @ResponseBody String getFutureTemp(@RequestParam double lat, @RequestParam double lng) throws Exception {
        if (!isValidLatAndLng(lat,lng)){
            throw new IllegalArgumentException("Bad query params to '/forecast' ");
        }
        return renderForecast(lat, lng, weatherService.getFutureTemp(new Coordinate(lat, lng)));
    }

    private String renderForecast(double lat, double lng, Map<Date, Double> forecast) {
        List<ForecastPresenter.Forecast> forecasts = new ArrayList<>();
        for (Map.Entry<Date, Double> kvp : forecast.entrySet()) {
            forecasts.add(new ForecastPresenter.Forecast(kvp.getKey().getTime(), kvp.getValue()));
        }
        return new ForecastPresenter(lat, lng, forecasts).toJson();
    }

    private boolean isValidLatAndLng(double lat, double lng) {
        return lat >= -90 && lat <= 90 && lng >= -180 && lng <= 180;
    }
}