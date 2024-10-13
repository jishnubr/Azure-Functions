package com.function;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class DataModel {
    @JacksonXmlProperty(localName = "Name")
    private String name;
    @JacksonXmlProperty(localName = "Value")
    private String value;

    // Getters and setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
