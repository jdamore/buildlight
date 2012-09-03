package com.suncorp.sintegration.buildlight.infrastructure;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class DriverWindowsImpl implements Driver {

    private static final String DRIVER = "USBCMDAP.exe";
    private static final Driver thisDriver = new DriverWindowsImpl();

    private Map<Integer, String> lightSids = new HashMap<Integer, String>();

    private DriverWindowsImpl() {}

    public static Driver getInstance() {
        return thisDriver;
    }

    public int setColor(int position, Color color) throws IOException {
        String lightSid = getLightSid(position);
        switch(color) {
            case OFF:    return changeStatus(lightSid, 0, 0);
            case BLUE:   return changeStatus(lightSid, 3, 1);
            case RED:    return changeStatus(lightSid, 5, 1);
            case GREEN:  return changeStatus(lightSid, 6, 1);
            case MULTI:  return changeStatus(lightSid, 4, 1);
            default: return -1;
        }
    }

    private String getLightSid(int position) throws IOException {
        String lightSid = this.lightSids.get(position);
        if(lightSid==null) {
            this.lightSids.put(position, readLightSid(position));
        }
        return this.lightSids.get(position);
    }

    private String readLightSid(int position) throws IOException {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec(DRIVER + " E");
            p.waitFor();
            return readDevicesProcessOutput(p)[position];
        }
        catch(InterruptedException e) {
            throw new RuntimeException("getLightSid: Problem waiting for runtime process to exit...", e);
        }
    }

    private String[] readDevicesProcessOutput(Process p) throws IOException {
        Vector<String> devices = new Vector<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            if(line.startsWith("Device Found")) {
                String[] tokens = line.split("SID=");
                devices.add(tokens[1]);
            }
            sb.append(line);
        }
        return devices.toArray(new String[]{""});
    }

    private int changeStatus(String lightSid, int lsb, int msb) throws IOException {
        Process p;
        try {
            Runtime runtime = Runtime.getRuntime();
            p = runtime.exec(DRIVER + " 2 " + lightSid + " 101 2 " + lsb + " " + msb);
            p.waitFor();
            return p.exitValue();
        }
        catch(InterruptedException e) {
            throw new RuntimeException("changeStatus: Problem waiting for runtime process to exit...", e);
        }
    }


}
