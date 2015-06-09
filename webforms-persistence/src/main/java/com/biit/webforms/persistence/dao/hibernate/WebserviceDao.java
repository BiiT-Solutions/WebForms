package com.biit.webforms.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.webforms.persistence.dao.IWebserviceDao;
import com.biit.webforms.persistence.entity.Webservice;

@Repository
public class WebserviceDao extends AnnotatedGenericDao<Webservice, Long> implements IWebserviceDao {

	public WebserviceDao() {
		super(Webservice.class);
	}

}
