package com.suncorp.sintegration.buildlight.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class Configuration {

    private String name;
    private Properties properties = null;

    public Configuration(String name) throws IOException {
        this.name = name;
        this.properties = new Properties();
        loadProperties();
    }

    public String getJenkinsUrl() {
        return properties.getProperty("jenkins.url");
    }

    private void loadProperties() throws IOException {
        URL url = ClassLoader.getSystemResource(this.name + ".properties");
        this.properties.load(new FileInputStream(new File(url.getFile())));
    }

    public String[] getJenkinsJobNames() {
        return properties.getProperty("jenkins.job.name").split(",");
    }

    public int getLightPosition() {
        return new Integer(properties.getProperty("light.position"));
    }
}
