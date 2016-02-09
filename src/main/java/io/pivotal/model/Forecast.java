package io.pivotal.model;

import lombok.Data;

@Data
public class Forecast {
    private final long time_epoch;
    private final double temp;
    private final String climacon_url;
    private final String climacon;
}
