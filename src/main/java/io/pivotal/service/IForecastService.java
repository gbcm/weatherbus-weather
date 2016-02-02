package io.pivotal.service;

import io.pivotal.Constants;
import retrofit.http.GET;
import retrofit.http.Path;

public interface IForecastService {
    @GET("/" + Constants.FORECAST_API_KEY + "/{latitude},{longitude}")
    ForecastResponse getForecast(@Path("latitude") String latitude, @Path("longitude") String longitude);
}
