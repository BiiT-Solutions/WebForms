package com.biit.webforms.persistence.dao;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Persistence)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.List;

import com.biit.form.entity.TreeObject;
import com.biit.persistence.dao.IJpaGenericDao;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.webforms.persistence.entity.Block;

public interface IBlockDao extends IJpaGenericDao<Block,Long> {

	Block getBlock(String blockLabel, Long organizationId) throws UnexpectedDatabaseException;

	List<Block> getAll(Long organizationId) throws UnexpectedDatabaseException;

	int getFormFlowsCountUsingElement(List<TreeObject> ids) throws UnexpectedDatabaseException;

	boolean checkIfBlockElementCanBeRemoved(TreeObject treeObject);
}
