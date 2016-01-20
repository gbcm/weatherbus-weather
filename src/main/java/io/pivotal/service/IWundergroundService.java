package io.pivotal.service;

import io.pivotal.Constants;
import retrofit.http.GET;
import retrofit.http.Path;

public interface IWundergroundService {
    @GET("/api/" + Constants.WUNDERGROUND_API_KEY + "/conditions/q/{latitude},{longitude}.json")
    WeatherConditionsResponse getConditionsResponse(@Path("latitude") String latitude, @Path("longitude") String longitude);

    @GET("/api/" + Constants.WUNDERGROUND_API_KEY + "/hourly/q/{latitude},{longitude}.json")
    WeatherForecastResponse getForecastResponse(@Path("latitude") String latitude, @Path("longitude") String longitude);
}
