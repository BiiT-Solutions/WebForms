package com.biit.webforms.configuration;

import net.sf.ehcache.util.FindBugsSuppressWarnings;

import com.biit.utils.configuration.ConfigurationReader;
import com.biit.utils.configuration.PropertiesSourceFile;
import com.biit.utils.configuration.SystemVariablePropertiesSourceFile;
import com.biit.utils.configuration.exception.PropertyNotFoundException;
import com.biit.webforms.logger.WebformsLogger;

public class WebformsConfigurationReader extends ConfigurationReader {
	
	private static final String DATABASE_CONFIG_FILE = "settings.conf";
	private static final String WEBFORMS_SYSTEM_VARIABLE_CONFIG = "WEBFORMS_CONFIG";
	
	private static final String ID_REGEX_EMAIL = "regexEmail";
	private static final String ID_REGEX_AMOUNT = "regexAmount";
	private static final String ID_REGEX_BIRTHDAY = "regexDateBirthday";
	private static final String ID_GRAPHVIZ_PATH = "graphvizBinPath";
	private static final String ID_REGEX_TEXT = "regexText";
	private static final String ID_REGEX_PHONE = "regexPhone";
	private static final String ID_REGEX_IBAN = "regexIban";
	private static final String ID_REGEX_BSN = "regexBsn";
	private static final String ID_REGEX_DATE = "regexDate";
	private static final String ID_REGEX_DATE_PERIOD = "regexDatePeriod";
	private static final String ID_REGEX_NUMBER = "regexNumber";
	private static final String ID_REGEX_FLOAT = "regexFloat";
	private static final String ID_REGEX_POSTAL_CODE = "regexPostalCode";
	private static final String ID_ISSUE_MANAGER_URL = "issueManagerUrl";
	private static final String ID_DATE_PATTERN = "dd/MM/yyyy";
	private static final String ID_BOOLEAN_SIMPLIFICATION_ENABLED = "booleanSimplificationEnabled";
	private static final String ID_XML_BASE_ADDRESS = "xmlBaseAddress";
	private static final String ID_BUILDING_BLOCK_LINKS = "button.link.block.visible";
	
	private static final String DEFAULT_REGEX_EMAIL = "[a-zA-Z!#$%&'*+\\-/=?^_`{|}~]+(\\.[a-zA-Z!#$%&'*+\\-/=?^_`{|}~]|[a-zA-Z!#$%&'*+\\-/=?^_`{|}~])*@[a-zA-Z0-9](\\.[a-zA-Z0-9-]|[a-zA-Z0-9-])*[a-zA-Z0-9]";
	private static final String DEFAULT_REGEX_AMOUNT = "[0-9]+\\.[0-9]*€";
	private static final String DEFAULT_REGEX_BIRTHDAY = "([0-9]{1,2}[-/]){1,2}[0-9]{4}";
	private static final String DEFAULT_GRAPHVIZ_PATH = "/usr/bin/dot";
	private static final String DEFAULT_REGEX_TEXT = "[\\u0000-\\uFFFF]*";
	private static final String DEFAULT_REGEX_PHONE = "[\\+]{0,1}[0-9]{5,14}";
	private static final String DEFAULT_REGEX_IBAN = "[A-Z]{2}[0-9]{2}( *[0-9A-Z]){1,34}";
	private static final String DEFAULT_REGEX_BSN = "[0-9]{9}";
	private static final String DEFAULT_REGEX_DATE = "([0-9]{1,2}[-/]){1,2}[0-9]{4}";
	private static final String DEFAULT_REGEX_DATE_PERIOD = "[0-9]+";
	private static final String DEFAULT_REGEX_NUMBER = "[0-9]+";
	private static final String DEFAULT_REGEX_FLOAT = "[0-9]+\\.[0-9]*";
	private static final String DEFAULT_REGEX_POSTAL_CODE = "[0-9]{4}[a-zA-Z]{2}";
	private static final String DEFAULT_ISSUE_MANAGER_URL = null;
	private static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";
	private static final String DEFAULT_BOOLEAN_SIMPLIFICATION_ENABLED = "false";
	private static final String DEFAULT_XML_BASE_ADDRESS = "http://dev.biit-solutions.com/";
	private static final String DEFAULT_BUILDING_BLOCK_LINKS = "true";

	// XForms
	private final String ID_XFORMS_USER = "orbeonUser";
	private final String ID_XFORMS_PASSWORD = "orbeonPassword";
	private final String ID_XFORMS_DATABASE = "orbeonDatabase";
	private final String ID_XFORMS_DATABASE_HOST = "orbeonDatabaseHost";
	private final String ID_XFORMS_FORM_RUNNER = "orbeonFormRunnerUrl";

	private final String DEFAULT_XFORMS_USER = "user";
	private final String DEFAULT_XFORMS_PASSWORD = "pass";
	private final String DEFAULT_XFORMS_DATABASE = "orbeon";	
	private final String DEFAULT_XFORMS_DATABASE_HOST = "localhost";
	private final String DEFAULT_XFORMS_FORM_RUNNER = "http://127.0.0.1:8080/orbeon/fr";

	private static WebformsConfigurationReader instance;

	private WebformsConfigurationReader() {
		super();

		addProperty(ID_REGEX_EMAIL, DEFAULT_REGEX_EMAIL);
		addProperty(ID_REGEX_AMOUNT, DEFAULT_REGEX_AMOUNT);
		addProperty(ID_REGEX_BIRTHDAY, DEFAULT_REGEX_BIRTHDAY);		
		addProperty(ID_GRAPHVIZ_PATH, DEFAULT_GRAPHVIZ_PATH);		
		addProperty(ID_REGEX_TEXT, DEFAULT_REGEX_TEXT);		
		addProperty(ID_REGEX_PHONE, DEFAULT_REGEX_PHONE);		
		addProperty(ID_REGEX_IBAN, DEFAULT_REGEX_IBAN);		
		addProperty(ID_REGEX_BSN, DEFAULT_REGEX_BSN);		
		addProperty(ID_REGEX_DATE, DEFAULT_REGEX_DATE);		
		addProperty(ID_REGEX_DATE_PERIOD, DEFAULT_REGEX_DATE_PERIOD);
		addProperty(ID_REGEX_NUMBER, DEFAULT_REGEX_NUMBER);
		addProperty(ID_REGEX_FLOAT, DEFAULT_REGEX_FLOAT);
		addProperty(ID_REGEX_POSTAL_CODE, DEFAULT_REGEX_POSTAL_CODE);
		addProperty(ID_ISSUE_MANAGER_URL, DEFAULT_ISSUE_MANAGER_URL);
		addProperty(ID_DATE_PATTERN, DEFAULT_DATE_PATTERN);
		addProperty(ID_BOOLEAN_SIMPLIFICATION_ENABLED, DEFAULT_BOOLEAN_SIMPLIFICATION_ENABLED);
		addProperty(ID_XML_BASE_ADDRESS, DEFAULT_XML_BASE_ADDRESS);
		addProperty(ID_BUILDING_BLOCK_LINKS, DEFAULT_BUILDING_BLOCK_LINKS);
		
		addProperty(ID_XFORMS_USER, DEFAULT_XFORMS_USER);
		addProperty(ID_XFORMS_PASSWORD, DEFAULT_XFORMS_PASSWORD);
		addProperty(ID_XFORMS_DATABASE, DEFAULT_XFORMS_DATABASE);
		addProperty(ID_XFORMS_DATABASE_HOST, DEFAULT_XFORMS_DATABASE_HOST);
		addProperty(ID_XFORMS_FORM_RUNNER, DEFAULT_XFORMS_FORM_RUNNER);

		addPropertiesSource(new PropertiesSourceFile(DATABASE_CONFIG_FILE));
		addPropertiesSource(new SystemVariablePropertiesSourceFile(WEBFORMS_SYSTEM_VARIABLE_CONFIG, DATABASE_CONFIG_FILE));

		readConfigurations();
	}

	@FindBugsSuppressWarnings("DC_DOUBLECHECK")
	public static WebformsConfigurationReader getInstance() {
		if (instance == null) {
			synchronized (WebformsConfigurationReader.class) {
				if (instance == null) {
					instance = new WebformsConfigurationReader();
				}
			}
		}
		return instance;
	}

	private String getPropertyLogException(String propertyId) {
		try {
			return getProperty(propertyId);
		} catch (PropertyNotFoundException e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			return null;
		}
	}
	
	public String getGraphvizBinPath() {
		return getPropertyLogException(ID_GRAPHVIZ_PATH);
	}

	public String getRegexText() {
		return getPropertyLogException(ID_REGEX_TEXT);
	}

	public String getRegexPhone() {
		return getPropertyLogException(ID_REGEX_PHONE);
	}

	public String getRegexIban() {
		return getPropertyLogException(ID_REGEX_IBAN);
	}

	public String getRegexBsn() {
		return getPropertyLogException(ID_REGEX_BSN);
	}

	public String getRegexDate() {
		return getPropertyLogException(ID_REGEX_DATE);
	}

	public String getRegexPostalCode() {
		return getPropertyLogException(ID_REGEX_POSTAL_CODE);
	}

	public String getRegexEmail() {
		return getPropertyLogException(ID_REGEX_EMAIL);
	}

	public String getRegexDatePeriod() {
		return getPropertyLogException(ID_REGEX_DATE_PERIOD);
	}

	public String getRegexNumber() {
		return getPropertyLogException(ID_REGEX_NUMBER);
	}

	public String getRegexFloat() {
		return getPropertyLogException(ID_REGEX_FLOAT);
	}

	public String getIssueManagerUrl() {
		return getPropertyLogException(ID_ISSUE_MANAGER_URL);
	}

	public String getRegexAmount() {
		return getPropertyLogException(ID_REGEX_AMOUNT);
	}

	public String getRegexDateBirthday() {
		return getPropertyLogException(ID_REGEX_BIRTHDAY);
	}

	public String getDatePattern() {
		return getPropertyLogException(ID_DATE_PATTERN);
	}

	/**
	 * Enable or disable the boolean simplification for reduce the length of
	 * Orbeon relevant rules.
	 * 
	 * @return
	 */
	public boolean isBooleanSimplificationEnabled() {
		return Boolean.parseBoolean(getPropertyLogException(ID_BOOLEAN_SIMPLIFICATION_ENABLED));
	}

	public String getXFormsUser() {
		return getPropertyLogException(ID_XFORMS_USER);
	}

	public String getXFormsPassword() {
		return getPropertyLogException(ID_XFORMS_PASSWORD);
	}

	public String getXFormsDatabaseName() {
		return getPropertyLogException(ID_XFORMS_DATABASE);
	}

	public String getXFormsDatabaseHost() {
		return getPropertyLogException(ID_XFORMS_DATABASE_HOST);
	}

	public String getOrbeonFormRunnerUrl() {
		return getPropertyLogException(ID_XFORMS_FORM_RUNNER);
	}

	public String getXmlBaseAddress() {
		return getPropertyLogException(ID_XML_BASE_ADDRESS);
	}

	public boolean isLinkBloksEnabled() {
		return Boolean.parseBoolean(getPropertyLogException(ID_BUILDING_BLOCK_LINKS));
	}
}
