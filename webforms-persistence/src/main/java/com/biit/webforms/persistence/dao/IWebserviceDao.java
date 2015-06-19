package com.biit.webforms.persistence.dao;

import java.util.Set;

import com.biit.webforms.persistence.dao.exceptions.WebserviceNotFoundException;
import com.biit.webforms.webservices.Webservice;

public interface IWebserviceDao {

	Set<Webservice> getAll();
	
	Webservice findWebservice(String name) throws WebserviceNotFoundException;
}
