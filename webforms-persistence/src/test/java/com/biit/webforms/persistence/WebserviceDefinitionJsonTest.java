package com.biit.webforms.persistence;

import java.util.Set;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.biit.webforms.persistence.dao.IWebserviceDao;
import com.biit.webforms.webservices.Webservice;
import com.biit.webforms.webservices.WebservicePort;
import com.biit.webforms.webservices.WebserviceValidatedPort;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "webserviceDefinitionJsonTest" })
public class WebserviceDefinitionJsonTest extends AbstractTransactionalTestNGSpringContextTests{

	private static final String WEBSERVICE_NAME = "validate_bsn";
	private static final String WEBSERVICE_DESCRIPTION = "ipsum lorem...";
	private static final String WEBSERVICE_PROTOCOL = "https";
	private static final String WEBSERVICE_HOST = "localhost";
	private static final String WEBSERVICE_PORT = "8081";
	private static final String WEBSERVICE_PATH = "rest/checkBsn";
	private static final String INPUT_PORT = "bsn";
	private static final String INPUT_XPATH = "";
	private static final String OUTPUT_PORT_1 = "name";
	private static final String OUTPUT_PORT_2 = "surname";
	private static final String OUTPUT_PORT_3 = "adress";
	private static final String OUTPUT_XPATH_1 = "/name";
	private static final String OUTPUT_XPATH_2 = "/surname";
	private static final String OUTPUT_XPATH_3 = "/adress";
	private static final String NOT_FOUND = "NOT_FOUND";
	private static final String INVALID_BSN = "INVALID_BSN";
	private static final String VALIDATION_XPATH = "/validation";
	
	@Autowired
	private IWebserviceDao webserviceDao;

	@Test
	public void testGenerateJsonAndParseBack(){
		Webservice webservice = new Webservice();
		webservice.setName(WEBSERVICE_NAME);
		webservice.setDescription(WEBSERVICE_DESCRIPTION);
		webservice.setProtocol(WEBSERVICE_PROTOCOL);
		webservice.setHost(WEBSERVICE_HOST);
		webservice.setPort(WEBSERVICE_PORT);
		webservice.setPath(WEBSERVICE_PATH);
		webservice.getInputPorts().add(new WebserviceValidatedPort(INPUT_PORT,INPUT_XPATH,VALIDATION_XPATH,NOT_FOUND,INVALID_BSN));
		webservice.getOutputPorts().add(new WebservicePort(OUTPUT_PORT_1,OUTPUT_XPATH_1));
		webservice.getOutputPorts().add(new WebservicePort(OUTPUT_PORT_2,OUTPUT_XPATH_2));
		webservice.getOutputPorts().add(new WebservicePort(OUTPUT_PORT_3,OUTPUT_XPATH_3));
		
		String jsonString = webservice.toJson();
		Assert.assertTrue(jsonString!=null);
		Assert.assertFalse(jsonString.isEmpty());
		
		Webservice parsedJson = Webservice.fromJson(jsonString);
		Assert.assertEquals(parsedJson.getName(), WEBSERVICE_NAME);
		Assert.assertEquals(parsedJson.getProtocol(), WEBSERVICE_PROTOCOL);
		Assert.assertEquals(parsedJson.getHost(), WEBSERVICE_HOST);
		Assert.assertEquals(parsedJson.getPort(), WEBSERVICE_PORT);
		
		for(WebserviceValidatedPort input : parsedJson.getInputPorts()){
			Assert.assertTrue(input.getName().equals(INPUT_PORT));
			Assert.assertTrue(input.getXpath().equals(INPUT_XPATH));
			Assert.assertTrue(input.getErrorCodes().contains(NOT_FOUND));
			Assert.assertTrue(input.getErrorCodes().contains(INVALID_BSN));
		}
		for(WebservicePort output : parsedJson.getOutputPorts()){
			Assert.assertTrue(output.getName().equals(OUTPUT_PORT_1)||output.getName().equals(OUTPUT_PORT_2)||output.getName().equals(OUTPUT_PORT_3));
			Assert.assertTrue(output.getXpath().equals(OUTPUT_XPATH_1)||output.getXpath().equals(OUTPUT_XPATH_2)||output.getXpath().equals(OUTPUT_XPATH_3));
		}
	}
	
	@Test
	public void testWebserviceDao(){
		Set<Webservice> webservices = webserviceDao.getAll();
		Assert.assertFalse(webservices.isEmpty());
	}
}
