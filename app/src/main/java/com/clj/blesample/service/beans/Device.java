package com.clj.blesample.service.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Device {

    //@JacksonXmlProperty(isAttribute = true)
    //private int id;
    @JacksonXmlProperty(localName = "name")
    private String name;
    @JacksonXmlProperty(localName = "mac")
    private String mac;
    @JacksonXmlProperty(localName = "uuid")
    private String uuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Device(String name, String mac, String uuid) {
        this.name = name;
        this.mac = mac;
        this.uuid = uuid;
    }

    public Device() {
    }

    @Override
    public String toString() {
        return String.format("name : %s | mac : %s | uuid : %s", getName(), getMac(), getUuid());
        //return "name:" + getName() + " | mac:" + getMac() + " | uuid:" + getUuid();
    }
}