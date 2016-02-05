package io.pivotal.view;

import io.pivotal.model.Forecast;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ForecastPresenter extends JsonPresenter {
    private final double latitude;
    private final double longitude;
    private final List<Forecast> forecast;
}
