package com.suncorp.sintegration.buildlight.application;

import com.suncorp.sintegration.buildlight.configuration.Configuration;

import java.io.IOException;
import java.io.PrintWriter;

public class BuildStatusMonitor {

    public Configuration config;
    private BuildStatusUpdateJob job;

    public BuildStatusMonitor(String configName) throws IOException {
        this.config = new Configuration(configName);
        this.job = new BuildStatusUpdateJob(config);
    }

    public void start() {
        try {
            while(true) {
                this.job.update();
                Thread.sleep(5000);
            }
        }
        catch(IOException e) {
            System.out.println("IOException caught in monitor");
            e.printStackTrace();
        }
        catch(InterruptedException e) {
            System.out.println("InterruptedException caught in monitor");
            e.printStackTrace();
        }
    }


}
