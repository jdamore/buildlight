package com.suncorp.sintegration.buildlight.domain;

import com.suncorp.sintegration.buildlight.infrastructure.Driver;

import java.io.IOException;

public class Light {

    private Driver driver;
    private int position;

    public Light(Driver driver, int position) {
        this.driver = driver;
        this.position = position;
    }

    public int setStatus(BuildStatus status) throws IOException {
        switch(status) {
            case OFF: return driver.setColor(position, Driver.Color.OFF);
            case SUCCESS: return driver.setColor(position, Driver.Color.GREEN);
            case FAILURE: return driver.setColor(position, Driver.Color.RED);
            case PROGRESS: return driver.setColor(position, Driver.Color.BLUE);
            default: return -1;
        }
    }
}
