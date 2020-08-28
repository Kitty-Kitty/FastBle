package com.clj.blesample.service.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Student {

    @JacksonXmlProperty(isAttribute = true)
    private int id;
    @JacksonXmlProperty(localName = "firstname")
    private String firstname;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @JacksonXmlProperty(localName = "lastname")
    private String lastname;
    @JacksonXmlProperty(localName = "major")
    private String major;
    @JacksonXmlProperty(localName = "department")
    private String department;

    public Student(int id, String firstname, String lastname, String major, String department) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.major = major;
        this.department = department;
    }

    public Student() {
    }

    public int getId() {
        return id;

    }
}