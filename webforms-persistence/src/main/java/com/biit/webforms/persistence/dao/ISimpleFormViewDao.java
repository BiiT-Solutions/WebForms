package com.biit.webforms.persistence.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.SimpleFormView;

public interface ISimpleFormViewDao {

	int getRowCount();

	List<SimpleFormView> getAll();

	void setSessionFactory(SessionFactory sessionFactory);

	SessionFactory getSessionFactory();

	List<SimpleFormView> getFormsThatUse(Block block);
	
}
