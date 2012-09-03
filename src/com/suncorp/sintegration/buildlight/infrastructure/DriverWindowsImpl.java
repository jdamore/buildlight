package com.suncorp.sintegration.buildlight.infrastructure;

import java.io.*;
import java.util.Vector;

public class DriverWindowsImpl implements Driver {

    public static final String DRIVER = "USBCMDAP.exe";

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
        Runtime runtime = Runtime.getRuntime();
        Process p = runtime.exec(DRIVER + " E");
        return readDevicesProcessOutput(p)[position];
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
            throw new RuntimeException("Problem waiting for runtime process to exit...", e);
        }
    }


}
