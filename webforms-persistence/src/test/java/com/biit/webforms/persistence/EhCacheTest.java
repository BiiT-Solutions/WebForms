package com.biit.webforms.persistence;

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
	private final static String DUMMY_FORM = "Dummy Form with cache";
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
