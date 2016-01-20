package io.pivotal.service;

/**
 * Created by pivotal on 1/13/16.
 */
public class WeatherConditionsResponseBuilder {

    public WeatherConditionsResponse build() {

        WeatherConditionsResponse.CurrentObservation observation = new WeatherConditionsResponse.CurrentObservation();
        observation.setTempF(36.2);

        WeatherConditionsResponse response = new WeatherConditionsResponse();
        response.setCurrentObservation(observation);

        return response;
    }
}
