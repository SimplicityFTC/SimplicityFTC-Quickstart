package org.simplicityftc.devices;

public class SimpleDigitalInput {
    private final int pin;
    private final Hub hub;

    public SimpleDigitalInput(Hub hub, int pin) {
        if(pin < 0 || pin > 5) throw new IllegalArgumentException("Port must be between 0 and 5");
        this.hub = hub;
        this.pin = pin;
    }

    public boolean getDigitalInput() {
        return hub.getLynxModule().getBulkData().getDigitalChannelState(pin);
    }
}
