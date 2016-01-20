package io.pivotal.view;

import lombok.Data;
import lombok.ToString;

/**
 * Created by pivotal on 1/6/16.
 */
@Data
@ToString
public class TemperaturePresenter extends JsonPresenter{
    private final Double temp;
    private final Double latitude;
    private final Double longitude;

    public TemperaturePresenter(Double latitude, Double longitude, Double temp) {
        this.temp = temp;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
