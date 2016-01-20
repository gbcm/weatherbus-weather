package io.pivotal.controller;

import io.pivotal.TestUtilities;
import io.pivotal.errorHandling.ErrorController;
import io.pivotal.errorHandling.ErrorPathConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by pivotal on 1/12/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class ErrorControllerTest {

    @InjectMocks
    ErrorController subject;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(subject).build();
    }

    @Test
    public void testGenericError() throws Exception {
        mockMvc.perform(get(ErrorPathConstants.ERROR_PATH))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/output/GenericError.json")));
    }

    @Test
    public void testNoQueryParamsError() throws Exception {
        mockMvc.perform(get(ErrorPathConstants.ERROR_NO_PARAMS_PATH))
                .andExpect(status().isBadRequest())
                .andExpect(
                        json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/output/WeatherNoParamError.json")));
    }

    @Test
    public void testOutOfRangeQueryParamsError() throws Exception {
        mockMvc.perform(get(ErrorPathConstants.ERROR_PARAM_OUT_OF_RANGE_PATH))
                .andExpect(status().isBadRequest())
                .andExpect(
                        json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/output/WeatherParamOutOfRangeError.json")));
    }

    @Test
    public void testRetrofitFailure() throws Exception {
        mockMvc.perform(get(ErrorPathConstants.ERROR_RETROFIT_CONFIG_PATH))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        json().isEqualTo(TestUtilities.jsonFileToString("src/test/resources/output/RetrofitError.json")));
    }

    @Test
    public void testBadJsonError() throws Exception {
        mockMvc.perform(get(ErrorPathConstants.JSON_SYNTAX_ERROR_PATH))
                .andExpect(status().isBadRequest())
                .andExpect(json().isEqualTo(TestUtilities.jsonFileToString(
                        "src/test/resources/output/BadJsonError.json")));
    }
}
