package com.biit.webforms.webservices;

import com.biit.webforms.serialization.WebserviceValidatedPortDeserializer;
import com.biit.webforms.serialization.WebserviceValidatedPortSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonDeserialize(using = WebserviceValidatedPortDeserializer.class)
@JsonSerialize(using = WebserviceValidatedPortSerializer.class)
public class WebserviceValidatedPort extends WebservicePort {

    private String validationXpath;
    private List<String> errorCodes;

    public WebserviceValidatedPort() {
        super();
    }

    public WebserviceValidatedPort(String name, String xpath, String validationXpath, String... values) {
        super(name, xpath);
        setValidationXpath(validationXpath);
        setErrorCodes(new ArrayList<>());
        getErrorCodes().addAll(Arrays.asList(values));
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<String> errorCodes) {
        this.errorCodes = errorCodes;
    }

    public String getValidationXpath() {
        return validationXpath;
    }

    public void setValidationXpath(String validationXpath) {
        this.validationXpath = validationXpath;
    }

}
