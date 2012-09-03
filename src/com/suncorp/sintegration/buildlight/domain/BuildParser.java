package com.suncorp.sintegration.buildlight.domain;

import java.io.IOException;


public interface BuildParser {

    public BuildStatus checkStatus(String url, String jobName) throws IOException;
}
