package com.suncorp.sintegration.buildlight.application;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.suncorp.sintegration.buildlight.configuration.Configuration;
import com.suncorp.sintegration.buildlight.domain.BuildParser;
import com.suncorp.sintegration.buildlight.domain.BuildParserJenkinsImpl;
import com.suncorp.sintegration.buildlight.domain.BuildStatus;
import com.suncorp.sintegration.buildlight.domain.Light;
import com.suncorp.sintegration.buildlight.infrastructure.DriverWindowsImpl;

public class BuildStatusUpdateJob {

    private Configuration config;
    private Light light;
    private String jenkinsUrl;
    private String[] jenkinsJobNames;

    public BuildStatusUpdateJob(Configuration config) {
        this.config = config;
        this.light = new Light(DriverWindowsImpl.getInstance(), this.config.getLightPosition());
        this.jenkinsUrl = config.getJenkinsUrl();
        this.jenkinsJobNames = config.getJenkinsJobNames();
    }

    public void update() throws IOException {
        Set statuses = new HashSet();
        for(String jenkinsJobName : this.jenkinsJobNames) {
            BuildParser buildParser = new BuildParserJenkinsImpl();
            jenkinsJobName = jenkinsJobName.trim();
            BuildStatus buildStatus = buildParser.checkStatus(this.jenkinsUrl, jenkinsJobName);
            System.out.println("Status for job " + jenkinsJobName + " is " + buildStatus);
            statuses.add(buildStatus);
        }
        BuildStatus overallStatus = getOverallStatus(statuses);
        System.out.println("Will set status of light at position " + config.getLightPosition() + " to " + overallStatus);
        this.light.setStatus(overallStatus);
    }

    private static BuildStatus getOverallStatus(Set<BuildStatus> statuses) {
        if( statuses.contains(BuildStatus.PROGRESS)) return BuildStatus.PROGRESS;
        else if( statuses.contains(BuildStatus.FAILURE)) return BuildStatus.FAILURE;
        else if( statuses.contains(BuildStatus.SUCCESS)) return BuildStatus.SUCCESS;
        else return BuildStatus.UNKNOWN;
    }
}
