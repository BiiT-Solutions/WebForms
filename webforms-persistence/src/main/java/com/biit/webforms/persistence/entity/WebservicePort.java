package com.biit.webforms.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "webservice_ports",uniqueConstraints =  { @UniqueConstraint(columnNames = { "name", "webservice"}) })
public class WebservicePort extends StorableObject {
	private static final long serialVersionUID = -1046884002321189451L;
	public static final int MAX_NAME_LENGTH = 100;
	public static final int MAX_DESCRIPTION_LENGTH = 250;
	
	@JoinColumn(name="webservice")
	@ManyToOne(optional = false)
	private Webservice webservice;
	
	@Column(length = MAX_NAME_LENGTH)
	private String name;
	
	@Column(length = MAX_DESCRIPTION_LENGTH)
	private String description;
	
	private String xpath;
	
	private String validationValues;

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		return new HashSet<StorableObject>();
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if(object instanceof WebservicePort){
			WebservicePort port = (WebservicePort) object;
			setName(port.getName());
			setDescription(port.getDescription());
			setXpath(port.getXpath());
			setValidationValues(port.getValidationValues());
		}else{
			throw new NotValidStorableObjectException("Element of class '"+object.getClass().getName()+"' is not compatible with '"+WebservicePort.class.getName()+"'");
		}
	}

	public Webservice getWebservice() {
		return webservice;
	}

	public void setWebservice(Webservice webservice) {
		this.webservice = webservice;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValidationValues() {
		return validationValues;
	}

	public void setValidationValues(String validationValues) {
		this.validationValues = validationValues;
	}

}
