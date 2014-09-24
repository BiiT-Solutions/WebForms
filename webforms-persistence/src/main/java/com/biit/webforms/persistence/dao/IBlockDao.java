package com.biit.webforms.persistence.dao;

import java.util.List;

import com.biit.form.persistence.dao.IBaseFormDao;
import com.biit.webforms.persistence.entity.Block;
import com.liferay.portal.model.Organization;

public interface IBlockDao extends IBaseFormDao<Block> {

	Block getBlock(String blockLabel, Organization organization);

	List<Block> getAll(Organization organization);
}
