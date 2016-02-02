package io.pivotal;

/**
 * Created by pivotal on 1/6/16.
 */
public class Constants {
    public static final String WUNDERGROUND_API_KEY = "1041f4934eaef072";
    public static final String WUNDERGROUND_ENDPOINT = "http://api.wunderground.com";
    public static final String FORECAST_API_KEY = "27c6d9bf92b7aa4d955516e76be4486d";
    public static final String FORECAST_ENDPOINT = "https://api.forecast.io/forecast";
    public static final int FORECAST_REQUEST_LIMIT = 1000;
    public static final long FORECAST_REQUEST_PERIOD_MILLISECONDS = 24 * 60 * 60 * 1000;
    public static final int WUNDERGROUND_REQUEST_LIMIT = 10;
    public static final long WUNDERGROUND_REQUEST_PERIOD_MILLISECONDS = 60 * 1000;
}
