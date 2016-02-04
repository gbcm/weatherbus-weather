package io.pivotal.service;

import retrofit.http.GET;
import retrofit.http.Path;

public interface IForecastService {
    @GET("/{latitude},{longitude}")
    ForecastResponse getForecast(@Path("latitude") String latitude, @Path("longitude") String longitude);
}
