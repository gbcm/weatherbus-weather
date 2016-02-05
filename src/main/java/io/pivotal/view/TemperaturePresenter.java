package io.pivotal.view;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper=false)
public class TemperaturePresenter extends JsonPresenter{
    private final Double latitude;
    private final Double longitude;
    private final Double temp;
    private final String climacon;
}