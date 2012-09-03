package com.suncorp.sintegration.buildlight.infrastructure;

import java.io.IOException;

public interface Driver {

    public enum Color { OFF, RED, GREEN, BLUE, MULTI }

    public int setColor(int position, Color color) throws IOException ;

}
