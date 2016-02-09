package io.pivotal.view;

import io.pivotal.model.WeatherIcon;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Data
@ToString
@EqualsAndHashCode(callSuper=false)
public class TemperaturePresenter extends JsonPresenter{
    private final Double latitude;
    private final Double longitude;
    private final Double temp;
    private final String climacon_url;
    private final String climacon;

    public TemperaturePresenter(HttpServletRequest request, double latitude, double longitude, double temp, String climacon_url, String climacon) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.temp = temp;
        this.climacon_url = WeatherIcon.getRemoteUrl(request, climacon_url);
        this.climacon = climacon;
    }
}