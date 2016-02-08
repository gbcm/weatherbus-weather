package io.pivotal.service;

import io.pivotal.Constants;
import io.pivotal.model.Coordinate;
import io.pivotal.model.Forecast;
import io.pivotal.model.WeatherIcon;
import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.UnknownServiceException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

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

    public Forecast getCurrentTemp(Coordinate coordinate) throws Exception {
        ServiceEndpoint endpoint = pickServiceEndpoint();

        switch (endpoint) {
            case WUNDERGROUND:
                WeatherConditionsResponse wcr = iWundergroundService
                        .getConditionsResponse(Double.toString(coordinate.getLatitude()),
                                Double.toString(coordinate.getLongitude()));
                return new Forecast(
                        new Date().getTime(),
                        wcr.getTempF(),
                        WeatherIcon.getIconFromWunderground(
                                wcr.getIcon()).getClimaconFileName());
            case FORECAST:
                return getForecastCurrentTemp(coordinate);
            default:
                throw new UnknownServiceException("No available services or quota!");
        }
    }

    public Forecast getForecastCurrentTemp(Coordinate coordinate) throws Exception {
        ForecastResponse fr = iForecastService.getForecast(Double.toString(coordinate.getLatitude()),
                Double.toString(coordinate.getLongitude()));
        return new Forecast(
                new Date().getTime(),
                fr.getCurrentTemperature(),
                WeatherIcon.getIconFromForecast(fr.getCurrently()
                        .getIcon()).getClimaconFileName());
    }

    public List<Forecast> getFutureTemp(Coordinate coordinate) throws Exception {
        ServiceEndpoint endpoint = pickServiceEndpoint();
        switch (endpoint) {
            case WUNDERGROUND:
                List<Forecast> forecasts = new ArrayList<>();
                WeatherForecastResponse wfr = iWundergroundService.getForecastResponse(
                        Double.toString(coordinate.getLatitude()),
                        Double.toString(coordinate.getLongitude()));
                forecasts.addAll(wfr.getHourlyForecasts().stream().map(
                        hf -> new Forecast(
                                Long.parseLong(hf.getFctTime().getEpoch()),
                                Double.parseDouble(hf.getTemp().getEnglish()),
                                WeatherIcon.getIconFromWunderground(
                                        hf.getWundergroundIcon()).getClimaconFileName()))
                        .collect(Collectors.toList()));
                return forecasts;
            case FORECAST:
                return getForecastFutureTemp(coordinate);
            default:
                throw new UnknownServiceException("No available services or quota!");
        }
    }

    public List<Forecast> getForecastFutureTemp(Coordinate coordinate) {
        List<Forecast> forecasts = new ArrayList<>();
        ForecastResponse fr = iForecastService.getForecast(Double.toString(coordinate.getLatitude()),
                Double.toString(coordinate.getLongitude()));
        forecasts.addAll(fr.getHourly().getData().stream().map(
                hf -> new Forecast(
                        hf.getTime(),
                        hf.getTemperature(),
                        WeatherIcon.getIconFromForecast(hf.getIcon()).getClimaconFileName()))
                .collect(Collectors.toList()));
        return forecasts;
    }
}
