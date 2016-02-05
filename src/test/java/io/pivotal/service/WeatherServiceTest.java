package io.pivotal.service;

import com.google.gson.Gson;
import io.pivotal.Constants;
import io.pivotal.TestUtilities;
import io.pivotal.model.Coordinate;
import io.pivotal.model.Forecast;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.UnknownServiceException;
import java.util.*;

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
        when(iForecastService.getForecast(
                Double.toString(coordinate.getLatitude()),
                Double.toString(coordinate.getLongitude()))).thenReturn(forecastResponse);

        wundergroundConditionsResponse = gson.fromJson(
                TestUtilities.fixtureReader("Wunderground_CurrentTemp"),
                WeatherConditionsResponse.class);
        wundergroundForecastResponse = gson.fromJson(
                TestUtilities.fixtureReader("Wunderground_FutureTemp"),
                WeatherForecastResponse.class);
        when(iWundergroundService.getConditionsResponse(
                Double.toString(coordinate.getLatitude()),
                Double.toString(coordinate.getLongitude())))
                .thenReturn(wundergroundConditionsResponse);
        when(iWundergroundService.getForecastResponse(
                Double.toString(coordinate.getLatitude()),
                Double.toString(coordinate.getLongitude())))
                .thenReturn(wundergroundForecastResponse);

        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void testWundergroundGetCurrentTemp() throws Exception {
        assertEquals(51.4, subject.getCurrentTemp(coordinate).getTemp(), 0);
        assertEquals("Cloud-Rain.svg", subject.getCurrentTemp(coordinate).getClimacon());
    }

    @Test
    public void testForecastGetCurrentTemp() throws Exception {
        assertEquals(54.01, subject.getForecastCurrentTemp(coordinate).getTemp(), 0);
        assertEquals("Sun.svg", subject.getForecastCurrentTemp(coordinate).getClimacon());
    }

    @Test
    public void testGetFutureTemp() throws Exception {
        List<Forecast> expectedForecasts = new ArrayList<Forecast>(){{
            add(new Forecast(1452211200L, 43.0, "Sun.svg"));
            add(new Forecast(1452214800L, 42.0, "Cloud.svg"));
            add(new Forecast(1452218400L, 41.0, "Cloud-Sun.svg"));
        }};

        assertEquals(expectedForecasts, subject.getFutureTemp(coordinate));
    }

    @Test
    public void testGetForecastFutureTemp() throws Exception {
        List<Forecast> expectedForecases = new ArrayList<Forecast>(){{
            add(new Forecast(1454364000L, 53.61, "Cloud-Sun.svg"));
            add(new Forecast(1454367600L, 54.3, "Sun.svg"));
            add(new Forecast(1454371200L, 53.72, "Moon.svg"));
        }};

        assertEquals(expectedForecases, subject.getForecastFutureTemp(coordinate));
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