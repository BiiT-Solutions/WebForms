package com.biit.webforms.persistence;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.dao.IFormDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "blockTest" })
public class BlockLinkTests extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private IFormDao formDao;

	@Autowired
	private IBlockDao blockDao;

	@Test
	public void createLinkedBlock() {

	}

	@Test
	public void addLinkedBlockToForm() {

	}

	@Test
	public void moveUpLinkedBlockInForm() {

	}

	@Test
	public void moveDownLinkedBlockInForm() {

	}

	@Test
	public void removeElementInLinkedBlockNotAllowed() {

	}

	@Test
	public void removeLinkedBlockFromForm() {

	}

	@Test
	public void removeFormDoesNotRemoveLinkedBlock() {

	}

	@Test
	public void flowInLinkedBlockIsNotAllowed() {

	}

	@Test
	public void flowFromLinkedBlockToElementIsAllowed() {

	}

	@Test
	public void flowbetweenLinkedBlocksIsNotAllowed() {

	}

}
