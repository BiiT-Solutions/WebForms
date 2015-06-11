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
import com.biit.webforms.persistence.entity.Webservice;
import com.biit.webforms.persistence.entity.WebserviceIoPort;
import com.biit.webforms.persistence.entity.WebserviceValidationPort;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "webserviceDefinitionJsonTest" })
public class WebserviceDefinitionJsonTest extends AbstractTransactionalTestNGSpringContextTests{

	private static final String WEBSERVICE_NAME = "validate_bsn";
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
	private static final String VALIDATION_PORT = "validation";
	private static final String VALIDATION_XPATH = "/validation";
	private static final String NOT_FOUND = "NOT_FOUND";
	private static final String INVALID_BSN = "INVALID_BSN";
	
	@Autowired
	private IWebserviceDao webserviceDao;

	@Test
	public void testGenerateJsonAndParseBack(){
		Webservice webservice = new Webservice();
		webservice.setName(WEBSERVICE_NAME);
		webservice.setProtocol(WEBSERVICE_PROTOCOL);
		webservice.setHost(WEBSERVICE_HOST);
		webservice.setPort(WEBSERVICE_PORT);
		webservice.setPath(WEBSERVICE_PATH);
		webservice.getInputPorts().add(new WebserviceIoPort(INPUT_PORT,INPUT_XPATH));
		webservice.getOutputPorts().add(new WebserviceIoPort(OUTPUT_PORT_1,OUTPUT_XPATH_1));
		webservice.getOutputPorts().add(new WebserviceIoPort(OUTPUT_PORT_2,OUTPUT_XPATH_2));
		webservice.getOutputPorts().add(new WebserviceIoPort(OUTPUT_PORT_3,OUTPUT_XPATH_3));
		webservice.getValidationPorts().add(new WebserviceValidationPort(VALIDATION_PORT,VALIDATION_XPATH,NOT_FOUND,INVALID_BSN));
		
		String jsonString = webservice.toJson();
		Assert.assertTrue(jsonString!=null);
		Assert.assertFalse(jsonString.isEmpty());
		
		Webservice parsedJson = Webservice.fromJson(jsonString);
		Assert.assertEquals(parsedJson.getName(), WEBSERVICE_NAME);
		Assert.assertEquals(parsedJson.getProtocol(), WEBSERVICE_PROTOCOL);
		Assert.assertEquals(parsedJson.getHost(), WEBSERVICE_HOST);
		Assert.assertEquals(parsedJson.getPort(), WEBSERVICE_PORT);
		
		for(WebserviceIoPort input : parsedJson.getInputPorts()){
			Assert.assertTrue(input.getName().equals(INPUT_PORT));
			Assert.assertTrue(input.getXpath().equals(INPUT_XPATH));
		}
		for(WebserviceIoPort output : parsedJson.getOutputPorts()){
			Assert.assertTrue(output.getName().equals(OUTPUT_PORT_1)||output.getName().equals(OUTPUT_PORT_2)||output.getName().equals(OUTPUT_PORT_3));
			Assert.assertTrue(output.getXpath().equals(OUTPUT_XPATH_1)||output.getXpath().equals(OUTPUT_XPATH_2)||output.getXpath().equals(OUTPUT_XPATH_3));
		}
		for(WebserviceValidationPort validation : parsedJson.getValidationPorts()){
			Assert.assertTrue(validation.getName().equals(VALIDATION_PORT));
			Assert.assertTrue(validation.getXpath().equals(VALIDATION_XPATH));
			Assert.assertTrue(validation.getErrorCodes().contains(NOT_FOUND));
			Assert.assertTrue(validation.getErrorCodes().contains(INVALID_BSN));
		}
	}
	
	@Test
	public void testWebserviceDao(){
		Set<Webservice> webservices = webserviceDao.getAll();
		Assert.assertFalse(webservices.isEmpty());
	}
}
