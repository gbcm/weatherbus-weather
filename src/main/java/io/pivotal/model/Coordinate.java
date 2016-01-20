package io.pivotal.model;

import lombok.Data;

/**
 * Created by pivotal on 1/12/16.
 */
@Data
public class Coordinate {
    private double latitude;
    private double longitude;

    public Coordinate(double latitude, double longitude) {
        if (latitude > 90 || latitude < -90) {
            throw new IllegalArgumentException("latitude must be in [-90, 90]");
        }

        if (longitude > 180 || longitude < -180) {
            throw new IllegalArgumentException("longitude must be in [-180, 180]");
        }

        this.latitude = latitude;
        this.longitude = longitude;
    }
}
