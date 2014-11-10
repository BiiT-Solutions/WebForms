package com.biit.webforms.persistence.dao;

import java.util.List;

import com.biit.form.persistence.dao.IBaseFormDao;
import com.biit.webforms.persistence.entity.Block;

public interface IBlockDao extends IBaseFormDao<Block> {

	Block getBlock(String blockLabel, Long organizationId);

	List<Block> getAll(Long organizationId);
}
