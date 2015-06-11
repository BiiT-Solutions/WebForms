package com.biit.webforms.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "webservice_call_input_link")
public class WebserviceCallInputLink extends WebserviceCallLink{
	private static final long serialVersionUID = 8534588762014331498L;
	
	protected WebserviceCallInputLink() {
		super();
	}
	
	public WebserviceCallInputLink(WebserviceIoPort port){
		super(port);
	}

	@Override
	public WebserviceCallLink generateCopy() throws NotValidStorableObjectException {
		WebserviceCallInputLink link = new WebserviceCallInputLink();
		link.copyData(this);
		return link;
	}
}
