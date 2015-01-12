package com.biit.webforms.persistence.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import com.biit.webforms.persistence.entity.SimpleBlockView;

public interface ISimpleBlockViewDao {

	int getRowCount();

	List<SimpleBlockView> getAll();

	List<SimpleBlockView> getAll(Long organizationId);

	void setSessionFactory(SessionFactory sessionFactory);

	SessionFactory getSessionFactory();

}
