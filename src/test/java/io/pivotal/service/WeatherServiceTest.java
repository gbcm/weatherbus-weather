package io.pivotal.service;

import com.google.gson.Gson;
import io.pivotal.Constants;
import io.pivotal.TestUtilities;
import io.pivotal.model.Coordinate;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.UnknownServiceException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceTest {
    @Mock
    IWundergroundService iWundergroundService;

    @Mock
    IForecastService iForecastService;

    @InjectMocks
    WeatherService subject;

    ForecastResponse forecastResponse;
    WeatherConditionsResponse wundergroundConditionsResponse;
    WeatherForecastResponse wundergroundForecastResponse;
    Coordinate coordinate = new Coordinate(45.23, -160.56);

    Gson gson = new Gson();

    @Before
    public void setup() throws Exception {
        forecastResponse = gson.fromJson(
                TestUtilities.fixtureReader("Forecast_CurrentTemp"),
                ForecastResponse.class);
        wundergroundConditionsResponse = gson.fromJson(
                TestUtilities.fixtureReader("Wunderground_CurrentTemp"),
                WeatherConditionsResponse.class);
        wundergroundForecastResponse = gson.fromJson(
                TestUtilities.fixtureReader("Wunderground_FutureTemp"),
                WeatherForecastResponse.class);

        when(iWundergroundService.getConditionsResponse(
                Double.toString(coordinate.getLatitude()),
                Double.toString(coordinate.getLongitude()))).thenReturn(wundergroundConditionsResponse);
        when(iWundergroundService.getForecastResponse(
                Double.toString(coordinate.getLatitude()),
                Double.toString(coordinate.getLongitude()))).thenReturn(wundergroundForecastResponse);
        when(iForecastService.getForecast(
                Double.toString(coordinate.getLatitude()),
                Double.toString(coordinate.getLongitude()))).thenReturn(forecastResponse);

        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void testWundergroundGetCurrentTemp() throws Exception {
        assertEquals(51.4, subject.getCurrentTemp(coordinate), 0);
    }

    @Test
    public void testForecastGetCurrentTemp() throws Exception {
        assertEquals(54.01, subject.getForecastCurrentTemp(coordinate), 0);
    }

    @Test
    public void testGetFutureTemp() throws Exception {
        Map<Date, Double> expectedTemperatures = new HashMap<Date, Double>() {{
            put(new Date(1452211200L), 43.0);
            put(new Date(1452214800L), 42.0);
            put(new Date(1452218400L), 41.0);
        }};

        assertEquals(expectedTemperatures, subject.getFutureTemp(coordinate));
    }

    @Test
    public void testGetForecastFutureTemp() throws Exception {
        Map<Date, Double> expectedTemperatures = new HashMap<Date, Double>() {{
            put(new Date(1454364000L), 53.61);
            put(new Date(1454367600L), 54.3);
            put(new Date(1454371200L), 53.72);
        }};

        assertEquals(expectedTemperatures, subject.getForecastFutureTemp(coordinate));
    }

    @Test
    public void testTooManyWundergroundCalls() throws Exception {
        for (int i = 0; i < (Constants.WUNDERGROUND_REQUEST_LIMIT); i++) {
            subject.getCurrentTemp(coordinate);
        }
        verify(iWundergroundService,times(10)).getConditionsResponse(any(),any());
        reset(iWundergroundService);
        subject.getCurrentTemp(coordinate);
        subject.getFutureTemp(coordinate);
        verify(iForecastService,times(2)).getForecast(any(),any());
        verify(iWundergroundService,never()).getConditionsResponse(any(),any());
    }

    @Test(expected = UnknownServiceException.class)
    public void testTooManyApiCalls() throws Throwable {
        try {
            for (int i = 0; i < (Constants.WUNDERGROUND_REQUEST_LIMIT + Constants.FORECAST_REQUEST_LIMIT + 1); i++) {
                subject.getCurrentTemp(coordinate);
            }
        } finally {
            assertEquals(mockingDetails(iWundergroundService).getInvocations().size(), Constants.WUNDERGROUND_REQUEST_LIMIT);
            assertEquals(mockingDetails(iForecastService).getInvocations().size(), Constants.FORECAST_REQUEST_LIMIT);
        }
    }

    @Test
    public void testWundergroundApiCallCounterProperlyDecrements() throws Exception {
        for (int i = 0; i < Constants.WUNDERGROUND_REQUEST_LIMIT; i++) {
            subject.getCurrentTemp(coordinate);
        }

        DateTimeUtils.setCurrentMillisOffset(Constants.WUNDERGROUND_REQUEST_PERIOD_MILLISECONDS + 1);

        subject.getFutureTemp(coordinate);

        assertEquals(Constants.WUNDERGROUND_REQUEST_LIMIT + 1, mockingDetails(iWundergroundService).getInvocations().size());
    }

    @Test
    public void testForecastApiCallCounterProperlyDecrements() throws Exception {
        for (int i = 0; i < Constants.WUNDERGROUND_REQUEST_LIMIT + Constants.FORECAST_REQUEST_LIMIT; i++) {
            subject.getCurrentTemp(coordinate);
        }

        DateTimeUtils.setCurrentMillisOffset(Constants.FORECAST_REQUEST_PERIOD_MILLISECONDS + 1);

        for (int i = 0; i < Constants.WUNDERGROUND_REQUEST_LIMIT; i++) {
            subject.getCurrentTemp(coordinate);
        }

        subject.getFutureTemp(coordinate);

        assertEquals(Constants.FORECAST_REQUEST_LIMIT + 1, mockingDetails(iForecastService).getInvocations().size());
    }
}