package io.pivotal.service;

import io.pivotal.model.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit.RetrofitError;

import java.net.UnknownServiceException;
import java.util.Date;
import java.util.Map;

/**
 * Created by pivotal on 1/6/16.
 */
@Component
public class WeatherService {

    @Autowired
    IWundergroundService service;

    public double getCurrentTemp(Coordinate coordinate) throws UnknownServiceException {
        try {
            return service
                    .getConditionsResponse(Double.toString(coordinate.getLatitude()),
                            Double.toString(coordinate.getLongitude()))
                    .getTempF();
        }
        catch (RetrofitError e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }

    public Map<Date, Double> getFutureTemp(Coordinate coordinate) throws UnknownServiceException {
        try {
            return service
                    .getForecastResponse(Double.toString(coordinate.getLatitude()),
                            Double.toString(coordinate.getLongitude()))
                    .getTemps();
        } catch (RetrofitError e) {
            e.printStackTrace();
            throw new UnknownServiceException(e.getMessage());
        }
    }
}
