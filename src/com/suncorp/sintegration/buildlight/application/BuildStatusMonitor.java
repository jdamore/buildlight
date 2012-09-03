package com.suncorp.sintegration.buildlight.application;

import com.suncorp.sintegration.buildlight.configuration.Configuration;

import java.io.IOException;
import java.io.PrintWriter;

public class BuildStatusMonitor {

    private Configuration config;
    private BuildStatusUpdateJob job;
    private int updateFrequency;


    public BuildStatusMonitor(String configName) throws IOException {
        this.config = new Configuration(configName);
        this.job = new BuildStatusUpdateJob(config);
        this.updateFrequency = config.getUpdateFrequency();
    }

    public void start() {
        try {
            while(true) {
                this.job.update();
                Thread.sleep(this.updateFrequency*1000);
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
