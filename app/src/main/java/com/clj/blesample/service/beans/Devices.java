package com.clj.blesample.service.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

public class Devices {
    @JacksonXmlProperty(localName = "device")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Device> devicesList = new ArrayList<Device>();

    public Devices() {

    }

    public List<Device> getDevicesList() {
        return devicesList;
    }

    public void setDevicesList(List<Device> devicesList) {
        this.devicesList = devicesList;
    }
}
