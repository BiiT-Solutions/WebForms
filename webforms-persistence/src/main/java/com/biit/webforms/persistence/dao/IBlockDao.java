package com.biit.webforms.persistence.dao;

import java.util.List;

import com.biit.form.persistence.dao.IBaseFormDao;
import com.biit.persistence.dao.IJpaGenericDao;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.webforms.persistence.entity.Block;

public interface IBlockDao extends IJpaGenericDao<Block,Long> {

	Block getBlock(String blockLabel, Long organizationId) throws UnexpectedDatabaseException;

	List<Block> getAll(Long organizationId) throws UnexpectedDatabaseException;

	int getFormFlowsCountUsingElement(List<Long> ids) throws UnexpectedDatabaseException;
}
