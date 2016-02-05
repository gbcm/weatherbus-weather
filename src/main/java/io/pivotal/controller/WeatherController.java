package io.pivotal.controller;

import io.pivotal.model.Coordinate;
import io.pivotal.model.Forecast;
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

@Controller
@RequestMapping("api")
public class WeatherController {
    @Autowired
    WeatherService weatherService;

    @RequestMapping(value = "/temp", produces = {"application/json"})
    public @ResponseBody String getCurrentTemp(@RequestParam double lat, @RequestParam double lng) throws Exception {
        if (!isValidLatAndLng(lat, lng)) {
            throw new IllegalArgumentException("Bad query params to '/' ");
        }
        Forecast f = weatherService.getCurrentTemp(new Coordinate(lat, lng));
        return new TemperaturePresenter(lat, lng, f.getTemp(), f.getClimacon()).toJson();
    }

    @RequestMapping(value = "/forecast", produces = {"application/json"})
    public @ResponseBody String getFutureTemp(@RequestParam double lat, @RequestParam double lng) throws Exception {
        if (!isValidLatAndLng(lat,lng)){
            throw new IllegalArgumentException("Bad query params to '/forecast' ");
        }
        return new ForecastPresenter(lat, lng, weatherService.getFutureTemp(new Coordinate(lat, lng))).toJson();
    }

    private boolean isValidLatAndLng(double lat, double lng) {
        return lat >= -90 && lat <= 90 && lng >= -180 && lng <= 180;
    }
}