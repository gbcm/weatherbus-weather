package io.pivotal.service;

import retrofit.http.GET;
import retrofit.http.Path;

public interface IWundergroundService {
    @GET("/conditions/q/{latitude},{longitude}.json")
    WeatherConditionsResponse getConditionsResponse(@Path("latitude") String latitude, @Path("longitude") String longitude);

    @GET("/hourly/q/{latitude},{longitude}.json")
    WeatherForecastResponse getForecastResponse(@Path("latitude") String latitude, @Path("longitude") String longitude);
}
