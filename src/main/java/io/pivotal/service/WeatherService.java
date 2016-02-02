package io.pivotal.service;

import io.pivotal.Constants;
import io.pivotal.model.Coordinate;
import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.UnknownServiceException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by pivotal on 1/6/16.
 */
@Component
public class WeatherService {

    @Autowired
    IWundergroundService iWundergroundService;

    @Autowired
    IForecastService iForecastService;

    private Queue<Long> apiTimeStamps = new ArrayBlockingQueue<>(Constants.WUNDERGROUND_REQUEST_LIMIT);
    private Queue<Long> forecastTimeStamps = new ArrayBlockingQueue<>(Constants.FORECAST_REQUEST_LIMIT);

    private ServiceEndpoint pickServiceEndpoint() {
        long now = DateTimeUtils.currentTimeMillis();
        synchronized (apiTimeStamps) {
            if (apiTimeStamps.size() < Constants.WUNDERGROUND_REQUEST_LIMIT) {
                apiTimeStamps.add(now);
                return ServiceEndpoint.WUNDERGROUND;
            }

            if (apiTimeStamps.peek() < (now - Constants.WUNDERGROUND_REQUEST_PERIOD_MILLISECONDS)) {
                apiTimeStamps.remove();
                apiTimeStamps.add(now);
                return ServiceEndpoint.WUNDERGROUND;
            }

            if (forecastTimeStamps.size() < Constants.FORECAST_REQUEST_LIMIT) {
                forecastTimeStamps.add(now);
                return ServiceEndpoint.FORECAST;
            }

            Calendar date = new GregorianCalendar();
            date.setTimeInMillis(now);
            date.set(Calendar.HOUR_OF_DAY, 0);
            date.set(Calendar.MINUTE, 0);
            date.set(Calendar.SECOND, 0);
            date.set(Calendar.MILLISECOND, 0);
            long midnight = date.getTimeInMillis();
            if (forecastTimeStamps.peek() < midnight) {
                long time;
                while (forecastTimeStamps.size() > 0) {
                    time = forecastTimeStamps.remove();
                    if (time >= midnight) {
                        forecastTimeStamps.add(time);
                        break;
                    }
                }

                forecastTimeStamps.add(now);
                return ServiceEndpoint.FORECAST;
            }
        }

        return ServiceEndpoint.NONE;
    }

    public double getCurrentTemp(Coordinate coordinate) throws Exception {
        ServiceEndpoint endpoint = pickServiceEndpoint();

        switch (endpoint) {
            case WUNDERGROUND:
                return iWundergroundService
                        .getConditionsResponse(Double.toString(coordinate.getLatitude()),
                                Double.toString(coordinate.getLongitude()))
                        .getTempF();
            case FORECAST:
                return getForecastCurrentTemp(coordinate);
            default:
                throw new UnknownServiceException("No available services or quota!");
        }
    }

    public double getForecastCurrentTemp(Coordinate coordinate) throws Exception {
        return iForecastService.getForecast(Double.toString(coordinate.getLatitude()),
                Double.toString(coordinate.getLongitude())).getCurrentTemperature();
    }

    public Map<Date, Double> getFutureTemp(Coordinate coordinate) throws Exception {
        ServiceEndpoint endpoint = pickServiceEndpoint();

        switch (endpoint) {
            case WUNDERGROUND:
                return iWundergroundService
                        .getForecastResponse(Double.toString(coordinate.getLatitude()),
                                Double.toString(coordinate.getLongitude()))
                        .getTemps();
            case FORECAST:
                return getForecastFutureTemp(coordinate);
            default:
                throw new UnknownServiceException("No available services or quota!");
        }
    }

    public Map<Date, Double> getForecastFutureTemp(Coordinate coordinate) {
        return iForecastService.getForecast(Double.toString(coordinate.getLatitude()),
                Double.toString(coordinate.getLongitude())).getHourlyTemperatures();
    }
}
