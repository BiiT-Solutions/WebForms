package com.biit.webforms.persistence.dao;

import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.SimpleFormView;
import com.biit.webforms.persistence.entity.SimpleFormViewWithContent;

import java.util.List;

public interface ISimpleFormViewDao {

	int getRowCount();

	List<SimpleFormView> getAll();

	List<SimpleFormView> getAllPublished();

	SimpleFormViewWithContent get(Long id);

	List<SimpleFormView> getFormsThatUse(Block block);
	
}
