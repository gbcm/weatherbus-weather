package io.pivotal.view;

import io.pivotal.model.Forecast;
import io.pivotal.model.WeatherIcon;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ForecastPresenter extends JsonPresenter {
    private double latitude;
    private double longitude;
    private List<Forecast> forecast;

    public ForecastPresenter(HttpServletRequest request, double latitude, double longitude, List<Forecast> forecast) {
        this.latitude = latitude;
        this.longitude = longitude;

        this.forecast = new ArrayList<>();
        for (Forecast f : forecast) {
            this.forecast.add(new Forecast(
                    f.getTime_epoch(),
                    f.getTemp(),
                    WeatherIcon.getRemoteUrl(request, f.getClimacon_url()),
                    f.getClimacon().toString()));
        }
    }
}
