package com.suncorp.sintegration.buildlight.application;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import com.suncorp.sintegration.buildlight.configuration.Configuration;
import com.suncorp.sintegration.buildlight.domain.BuildParser;
import com.suncorp.sintegration.buildlight.domain.BuildParserJenkinsImpl;
import com.suncorp.sintegration.buildlight.domain.BuildStatus;
import com.suncorp.sintegration.buildlight.domain.Light;
import com.suncorp.sintegration.buildlight.infrastructure.DriverWindowsImpl;

public class BuildStatusUpdateJob {

    private Configuration config;

    public BuildStatusUpdateJob(Configuration config) {
        this.config = config;
    }

    public void update() throws IOException {
        String jenkinsUrl = config.getJenkinsUrl();
        Set statuses = new HashSet();
        String[] jenkinsJobNames = config.getJenkinsJobNames();
//        printJenkinsJobNames(jenkinsJobNames);
        for(String jenkinsJobName : jenkinsJobNames) {
            BuildParser buildParser = new BuildParserJenkinsImpl();
            jenkinsJobName = jenkinsJobName.trim();
            BuildStatus buildStatus = buildParser.checkStatus(jenkinsUrl, jenkinsJobName);
            System.out.println("Status for job " + jenkinsJobName + " is " + buildStatus);
            statuses.add(buildStatus);
        }
        BuildStatus overallStatus = getOverallStatus(statuses);
        Light light = new Light(new DriverWindowsImpl(), config.getLightPosition());
        System.out.println("Will set status of light at position " + config.getLightPosition() + " to " + overallStatus);
        light.setStatus(overallStatus);
    }

    private void printJenkinsJobNames(String[] jenkinsJobNames) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(String jenkinsJobName : jenkinsJobNames) {
            sb.append(jenkinsJobName + " ");
        }
        sb.append("]");
        System.out.println("Will check job for " + sb.toString());
    }

    private static BuildStatus getOverallStatus(Set<BuildStatus> statuses) {
        if( statuses.contains(BuildStatus.PROGRESS)) return BuildStatus.PROGRESS;
        else if( statuses.contains(BuildStatus.FAILURE)) return BuildStatus.FAILURE;
        else if( statuses.contains(BuildStatus.SUCCESS)) return BuildStatus.SUCCESS;
        else return BuildStatus.UNKNOWN;
    }
}
