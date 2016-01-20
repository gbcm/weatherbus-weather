package io.pivotal.service;

import com.google.gson.Gson;
import io.pivotal.TestUtilities;
import io.pivotal.model.Coordinate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceTest {
    @Mock
    IWundergroundService mockService;
    @InjectMocks
    WeatherService subject;

    Gson gson = new Gson();

    @Test
    public void testGetCurrentTemp() throws Exception {
        Double latitude = 45.23;
        Double longitude = -160.56;

        WeatherConditionsResponse response = gson.fromJson(
                TestUtilities.fixtureReader("CurrentTemp"),
                WeatherConditionsResponse.class);

        when(mockService.getConditionsResponse(
                latitude.toString(),
                longitude.toString())).thenReturn(response);

        assertEquals(51.4, subject.getCurrentTemp(new Coordinate(latitude, longitude)), 0);
    }

    @Test
    public void testGetFutureTemp() throws Exception {
        Double latitude = 45.23;
        Double longitude = -160.56;

        WeatherForecastResponse response = gson.fromJson(
                TestUtilities.fixtureReader("FutureTemp"),
                WeatherForecastResponse.class);

        when(mockService.getForecastResponse(
                latitude.toString(),
                longitude.toString())).thenReturn(response);
        Map<Date, Double> expectedTemperatures = new HashMap<Date, Double>() {{
            put(new Date(1452211200L), 43.0);
            put(new Date(1452214800L), 42.0);
            put(new Date(1452218400L), 41.0);
        }};

        assertEquals(expectedTemperatures, subject.getFutureTemp(new Coordinate(latitude, longitude)));
    }
}