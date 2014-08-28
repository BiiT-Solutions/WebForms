package com.biit.webforms.persistence.dao;

import com.biit.form.persistence.dao.IBaseFormDao;
import com.biit.webforms.persistence.entity.Block;

public interface IBlockDao extends IBaseFormDao<Block> {

	Block getBlock(String blockName);

}
