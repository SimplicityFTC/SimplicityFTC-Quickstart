package org.simplicityftc.devices;

public class SimpleAnalogInput {
    private final int pin;
    private final Hub hub;

    public SimpleAnalogInput(Hub hub, int pin) {
        if(pin < 0 || pin > 5) throw new IllegalArgumentException("Port must be between 0 and 5");
        this.hub = hub;
        this.pin = pin;

    }

    public double getAnalogInput() {
        return hub.getLynxModule().getBulkData().getAnalogInputVoltage(pin);
    }
}
