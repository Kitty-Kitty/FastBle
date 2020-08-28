package com.clj.blesample.service.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "configuration")
public class Configuration {
    /**
     * Getter for property 'devices'.
     *
     * @return Value for property 'devices'.
     */
    public Devices getDevices() {
        return devices;
    }

    /**
     * Setter for property 'devices'.
     *
     * @param devices Value to set for property 'devices'.
     */
    public void setDevices(Devices devices) {
        this.devices = devices;
    }

    @JacksonXmlProperty(localName = "devices")
    private Devices devices = new Devices();

    /**
     * Getter for property 'bleConfiguration'.
     *
     * @return Value for property 'bleConfiguration'.
     */
    public BleConfiguration getBleConfiguration() {
        return bleConfiguration;
    }

    /**
     * Setter for property 'bleConfiguration'.
     *
     * @param bleConfiguration Value to set for property 'bleConfiguration'.
     */
    public void setBleConfiguration(BleConfiguration bleConfiguration) {
        this.bleConfiguration = bleConfiguration;
    }

    @JacksonXmlProperty(localName = "bleConfiguration")
    private BleConfiguration bleConfiguration = new BleConfiguration();


    public Configuration() {

    }

}