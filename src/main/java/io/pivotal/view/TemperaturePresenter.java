package io.pivotal.view;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper=false)
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