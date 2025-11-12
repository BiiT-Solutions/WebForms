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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.persistence.dao.IFormDao;
import com.biit.webforms.persistence.entity.Form;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "ehCache" })
@DirtiesContext
public class EhCacheTest extends AbstractTransactionalTestNGSpringContextTests {
	private static final String DUMMY_FORM = "Dummy Form with cache";
	private static final Long ORGANIZATION_ID = 0L;

	@Autowired
	private IFormDao formDao;

//	@Test
//	public void testSecondLevelCache() throws FieldTooLongException, UnexpectedDatabaseException,
//			ElementCannotBeRemovedException, ElementCannotBePersistedException {
//		Form form = new Form();
//		form.setLabel(DUMMY_FORM);
//		form.setOrganizationId(ORGANIZATION_ID);
//		formDao.makePersistent(form);
//
//		// fetch the form entity from database first time
//		form = formDao.getForm(DUMMY_FORM, ORGANIZATION_ID);
//		Assert.assertNotNull(form);
//
//		Assert.assertEquals(formDao.getSessionFactory().getStatistics().getEntityFetchCount(), 0);
//		Assert.assertEquals(formDao.getSessionFactory().getStatistics().getSecondLevelCacheMissCount(), 1);
//		Assert.assertEquals(formDao.getSessionFactory().getStatistics().getSecondLevelCacheHitCount(), 0);
//
//		// Here entity is already in second level cache (session has been closed) so no database query will be hit
//		form = formDao.getForm(DUMMY_FORM, ORGANIZATION_ID);
//		Assert.assertNotNull(form);
//
//		Assert.assertEquals(formDao.getSessionFactory().getStatistics().getEntityFetchCount(), 0);
//		Assert.assertEquals(formDao.getSessionFactory().getStatistics().getSecondLevelCacheHitCount(), 1);
//
//		// Read by id, not by label. Still cached.
//		form = formDao.read(form.getId());
//		Assert.assertNotNull(form);
//
//		Assert.assertEquals(formDao.getSessionFactory().getStatistics().getEntityFetchCount(), 0);
//		Assert.assertEquals(formDao.getSessionFactory().getStatistics().getSecondLevelCacheHitCount(), 2);
//
//		// Removed forms also are removed from cache.
//		long id = form.getId();
//		formDao.makeTransient(form);
//		form = formDao.read(id);
//		Assert.assertNull(form);
//
//	}
}
