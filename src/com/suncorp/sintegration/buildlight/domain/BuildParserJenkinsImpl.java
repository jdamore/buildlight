package com.suncorp.sintegration.buildlight.domain;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;


public class BuildParserJenkinsImpl implements BuildParser {

    public BuildStatus checkStatus(String url, String jobName) throws IOException {
        String status = getJobStatus(url, jobName);
        if (status.equals("blue_anime")) return BuildStatus.PROGRESS;
        else if (status.equals("red_anime")) return BuildStatus.PROGRESS;
        else if (status.equals("red")) return BuildStatus.FAILURE;
        else if (status.equals("yellow")) return BuildStatus.FAILURE;
        else if (status.equals("blue")) return BuildStatus.SUCCESS;
        return BuildStatus.UNKNOWN;
    }

    private String getJobStatus(String url, String jobName) throws IOException {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod("http://"+url+"/api/xml");
        NameValuePair xpath= new NameValuePair("xpath","/hudson/job[name='"+jobName+"']/color/text()");
        method.setQueryString(new NameValuePair[]{xpath});
        client.executeMethod(method);
        return method.getResponseBodyAsString();
    }

}
