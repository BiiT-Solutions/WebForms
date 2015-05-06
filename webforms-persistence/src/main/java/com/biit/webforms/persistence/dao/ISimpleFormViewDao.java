package com.biit.webforms.persistence.dao;

import java.util.List;

import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.SimpleFormView;

public interface ISimpleFormViewDao {

	int getRowCount();

	List<SimpleFormView> getAll();

	List<SimpleFormView> getFormsThatUse(Block block);
	
}
