package com.biit.webforms.webservices;

import com.biit.webforms.enumerations.AnswerFormat;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.serialization.WebservicePortDeserializer;
import com.biit.webforms.serialization.WebservicePortSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonDeserialize(using = WebservicePortDeserializer.class)
@JsonSerialize(using = WebservicePortSerializer.class)
public class WebservicePort {

    private String name;

    private String xpath;

    private AnswerType type;
    private AnswerFormat format;
    private AnswerSubformat subformat;

    public WebservicePort() {

    }

    public WebservicePort(String name, String xpath) {
        setName(name);
        setXpath(xpath);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public AnswerType getType() {
        return type;
    }

    public void setType(AnswerType type) {
        this.type = type;
    }

    public AnswerFormat getFormat() {
        return format;
    }

    public void setFormat(AnswerFormat format) {
        this.format = format;
    }

    public AnswerSubformat getSubformat() {
        return subformat;
    }

    public void setSubformat(AnswerSubformat subformat) {
        this.subformat = subformat;
    }
}
