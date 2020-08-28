package com.clj.blesample.service.beans;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "studentinfo")
public class StudentInfo {
    @JacksonXmlProperty(localName = "student")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Student[] students;

    public StudentInfo(Student[] students) {
        this.students = students;
    }

    public StudentInfo() {

    }

    public Student[] getStudents() {
        return students;
    }

    public void setStudents(Student[] students) {
        this.students = students;
    }
}