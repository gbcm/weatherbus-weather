package io.pivotal.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum WeatherIcon {
    UNKNOWN("Shades.svg","unknown",""),
    CHANCE_LIGHT_SNOW("Snowflake.svg","chanceflurries",""),
    CHANCE_RAIN("Cloud-Drizzle.svg","chancerain",""),
    CHANCE_SLEET("Cloud-Hail.svg","chancesleet",""),
    CHANCE_SNOW("Cloud-Snow.svg","chancesnow",""),
    CHANCE_STORM("Cloud-Lightning.svg","chancetstorms",""),
    CLEAR_DAY("Sun.svg","clear","clear-day"),
    CLOUDY("Cloud.svg","cloudy","cloudy"),
    LIGHT_SNOW("Cloud-Snow.svg","flurries",""),
    FOG("Cloud-Fog.svg","fog","fog"),
    HAZE("Cloud-Fog.svg","hazy",""),
    MOSTLY_CLOUDY("Cloud-Sun.svg","mostlycloudy",""),
    MOSTLY_SUNNY("Cloud-Sun.svg","mostlysunny",""),
    PARTLY_CLOUDY("Cloud-Sun.svg","partlycloudy",""),
    PARTLY_SUNNY("Cloud-Sun.svg","partlysunny","partly-cloudy-day"),
    SLEET("Cloud-Hail.svg","sleet","sleet"),
    RAIN("Cloud-Rain.svg","rain","rain"),
    SNOW("Cloud-Snow.svg","snow","snow"),
    SUNNY("Sun.svg","sunny",""),
    STORM("Cloud-Lightning.svg","tstorms",""),
    CLEAR_NIGHT("Moon.svg","","clear-night"),
    WIND("Wind.svg","","wind"),
    PARTLY_CLOUDY_NIGHT("Cloud-Moon.svg","","partly-cloudy-night")
    ;

    @Getter
    private final String climaconFileName;
    @Getter
    private final String wundergroundIcon;
    @Getter
    private final String forecastIcon;

    WeatherIcon(String climaconFileName, String wundergroundIcon, String forecastIcon) {
        this.climaconFileName = climaconFileName;
        this.wundergroundIcon = wundergroundIcon;
        this.forecastIcon = forecastIcon;
    }

    private static final Map<String, WeatherIcon> wundergroundLookup = new HashMap<>();
    private static final Map<String, WeatherIcon> forecastLookup = new HashMap<>();

    static {
        for (WeatherIcon w : WeatherIcon.values()) {
            wundergroundLookup.put(w.getWundergroundIcon(), w);
            forecastLookup.put(w.getForecastIcon(), w);
        }
    }

    public static WeatherIcon getIconFromWunderground(String wundergroundIcon){
        return wundergroundLookup.get(wundergroundIcon);
    }

    public static WeatherIcon getIconFromForecast(String forecastIcon){
        return wundergroundLookup.get(forecastIcon);
    }
}
