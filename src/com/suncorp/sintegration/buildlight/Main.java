package com.suncorp.sintegration.buildlight;

import com.suncorp.sintegration.buildlight.application.BuildStatusMonitor;

public class Main {

    public static void main(String[] args) throws Exception {
        String configName = args[0];
        BuildStatusMonitor monitor = new BuildStatusMonitor(configName);
        monitor.start();
    }
}
