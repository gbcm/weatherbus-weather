package io.pivotal.controller;

import io.pivotal.TestUtilities;
import io.pivotal.errorHandling.TooManyRequestsException;
import io.pivotal.model.Coordinate;
import io.pivotal.model.Forecast;
import io.pivotal.service.WeatherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.util.NestedServletException;
import retrofit.RetrofitError;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(MockitoJUnitRunner.class)
public class WeatherControllerTest {
    @Mock
    WeatherService weatherService;
    @Mock
    HandlerExceptionResolver handlerExceptionResolver;
    @InjectMocks
    WeatherController subject;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .setHandlerExceptionResolvers(handlerExceptionResolver)
                .build();
    }

    @Test
    public void testGetCurrentTemp() throws Exception {
        when(weatherService.getCurrentTemp(new Coordinate(47.6098, -122.3332))).thenReturn(
                new Forecast(0L, 36.2, "Cloud-Snow.svg", "SNOW")
        );
        mockMvc.perform(get("/api/temp?lat=47.6098&lng=-122.3332")).andExpect(
                json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/output/CurrentTemp.json")));
    }

    @Test
    public void testGetFutureTemp() throws Exception {
        List<Forecast> values = new ArrayList<Forecast>() {{
            add(new Forecast(1452222000L, 14.4, "Cloud-Fog.svg", "FOG"));
            add(new Forecast(1452225600L, 15.5, "Cloud-Hail.svg", "SLEET"));
        }};

        when(weatherService.getFutureTemp(new Coordinate(47.6098, -122.3332))).thenReturn(values);
        mockMvc.perform(get("/api/forecast?lat=47.6098&lng=-122.3332")).andExpect(
                json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/output/FutureTemp.json")));
    }

    @Test(expected = ServletException.class)
    public void testGetCurrentTempNetworkFailure() throws Exception {
        when(weatherService.getCurrentTemp(new Coordinate(47.6097, -122.3331))).
                thenThrow(RetrofitError.networkError("Whateva", new IOException()));
        mockMvc.perform(get("/api/temp?lat=47.6097&lng=-122.3331"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFutureTempWithOutOfRangeParams() throws Throwable {
        try {
            mockMvc.perform(get("/api/forecast?lat=70.6098&lng=-1220.3332"));
        } catch (NestedServletException e) {
            throw e.getCause();
        } finally {
            verifyNoMoreInteractions(weatherService);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTempWithOutOfRangeParams() throws Throwable {
        try {
            mockMvc.perform(get("/api/temp?lat=-90.6098&lng=180.3332"));
        } catch (NestedServletException e) {
            throw e.getCause();
        } finally {
            verifyNoMoreInteractions(weatherService);
        }
    }

    @Test(expected = MissingServletRequestParameterException.class)
    public void testGetTempWithOutParams() throws Throwable {
        try {
            mockMvc.perform(get("/api/temp"));
        } finally {
            verifyNoMoreInteractions(weatherService);
        }
    }

    @SuppressWarnings("unchecked")
    @Test(expected = TooManyRequestsException.class)
    public void testTooManyRequests() throws Throwable {
        when(weatherService.getCurrentTemp(any(Coordinate.class))).thenThrow(TooManyRequestsException.class);
        try {
            mockMvc.perform(get("/api/temp?lat=47.6097&lng=-122.3331"));
        } catch (NestedServletException e) {
            throw e.getCause();
        }
    }
}
