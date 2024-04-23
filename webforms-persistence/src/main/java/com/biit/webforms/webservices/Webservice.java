package com.biit.webforms.webservices;

import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Webservice {

    private String name;

    private String description;

    private String protocol;

    private String host;

    private String port;

    private String path;

    private Set<WebserviceValidatedPort> inputPorts;

    private Set<WebservicePort> outputPorts;

    public Webservice() {
        super();
        inputPorts = new HashSet<>();
        outputPorts = new HashSet<>();
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return getProtocol() + "://" + getHost() + ":" + getPort() + "/" + getPath();
    }

    public Set<WebserviceValidatedPort> getInputPorts() {
        return inputPorts;
    }

    public WebserviceValidatedPort getInputPort(String name) {
        for (WebserviceValidatedPort input : getInputPorts()) {
            if (Objects.equals(input.getName(), name)) {
                return input;
            }
        }
        return null;
    }

    public void setInputPorts(Set<WebserviceValidatedPort> inputPorts) {
        this.inputPorts = inputPorts;
    }

    public Set<WebservicePort> getOutputPorts() {
        return outputPorts;
    }

    public WebservicePort getOutputPort(String name) {
        for (WebservicePort output : getOutputPorts()) {
            if (Objects.equals(output.getName(), name)) {
                return output;
            }
        }
        return null;
    }

    public void setOutputPorts(Set<WebservicePort> outputPorts) {
        this.outputPorts = outputPorts;
    }

    public String toJson() {
        try {
            return ObjectMapperFactory.getObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Webservice fromJson(String jsonString) throws JsonProcessingException {
        return ObjectMapperFactory.getObjectMapper().readValue(jsonString, Webservice.class);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private WebservicePort findPort(WebserviceCallLink link, Set<WebservicePort> ports) {
        for (WebservicePort port : ports) {
            if (link.getWebservicePort().equals(port.getName())) {
                return port;
            }
        }
        return null;
    }

    public WebservicePort findOutputPort(WebserviceCallOutputLink link) {
        return findPort(link, getOutputPorts());
    }

    public WebserviceValidatedPort findInputPort(WebserviceCallInputLink link) {
        return (WebserviceValidatedPort) findPort(link, new HashSet<WebservicePort>(getInputPorts()));
    }
}
