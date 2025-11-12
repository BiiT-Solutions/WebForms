package com.biit.webforms.persistence;

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

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContextTest.xml"})
@Test(groups = {"blockTest"})
public class BlockTests extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private IFormDao formDao;

    @Autowired
    private IBlockDao blockDao;

    private Block block;

    public void createBuildingBlock() throws NotValidChildException, FieldTooLongException,
            CharacterNotAllowedException, InvalidAnswerFormatException, InvalidAnswerSubformatException,
            UnexpectedDatabaseException, ElementIsReadOnly, ElementCannotBePersistedException {
        if (block == null) {
            block = FormUtils.createBlock();
            block.setLabel("CacheBlock");
            Assert.assertNotNull(block);
            blockDao.makePersistent(block);
        }
    }

    @Test
    public void blockCache() throws NotValidChildException, FieldTooLongException, CharacterNotAllowedException,
            InvalidAnswerFormatException, InvalidAnswerSubformatException, UnexpectedDatabaseException,
            ElementIsReadOnly, ElementCannotBePersistedException {
        createBuildingBlock();

        int elements = block.getAllInnerStorableObjects().size();

        TreeObject question = FormUtils.createQuestionAnswers("NewQuestion", AnswerType.INPUT);
        TreeObject category = block.getChildren().get(0);
        category.addChild(question);

        Assert.assertEquals(elements + 1, block.getAllInnerStorableObjects().size());

        blockDao.merge(block);

        Block cachedBlock = blockDao.get(block.getId());
        Assert.assertEquals(elements + 1, cachedBlock.getAllInnerStorableObjects().size());
    }

}
