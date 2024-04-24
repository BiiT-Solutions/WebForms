package com.biit.webforms.serialization;

import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

public class WebserviceCallInputLinkSerializer extends WebserviceCallLinkSerializer<WebserviceCallInputLink> {

	@Override
	public void serialize(WebserviceCallInputLink src, JsonGenerator jgen) throws IOException {
		super.serialize(src, jgen);
		jgen.writeObjectField("errors", src.getErrors());
		jgen.writeStringField("validationXpath", src.getValidationXpath());
	}

}