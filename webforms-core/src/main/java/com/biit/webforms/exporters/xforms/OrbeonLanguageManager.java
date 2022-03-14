package com.biit.webforms.exporters.xforms;

import java.io.File;


import com.biit.utils.annotations.FindBugsSuppressWarnings;
import com.biit.utils.configuration.ConfigurationReader;
import com.biit.utils.configuration.PropertiesSourceFile;
import com.biit.utils.configuration.exceptions.PropertyNotFoundException;
import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.logger.WebformsLogger;

public class OrbeonLanguageManager extends ConfigurationReader {
    private static final OrbeonLanguage[] languages = OrbeonLanguage.values();
    private static final String LANGUAGE_CONFIG_FOLDER = "languages";
    private static final String LANGUAGE_CONFIG_FILE = "orbeon.properties";

    private static final String ORBEON_PREFIX = "orbeon.alert";
    private static final String ALERT_DATE_FUTURE = "date.future";
    private static final String ALERT_DATE_PAST = "date.past";
    private static final String ALERT_BSN = "bsn";
    private static final String ALERT_IBAN = "iban";
    private static final String ALERT_PHONE = "phone";
    private static final String ALERT_POSTAL_CODE = "postal.code";
    private static final String INVALID_NUMBER_CODE = "invalid.number";
    private static final String ALERT_DEFAULT = "default";

    private static OrbeonLanguageManager instance;

    private OrbeonLanguageManager() {
        super();

        for (OrbeonLanguage language : languages) {
            addProperty(getRealTag(language, ALERT_DATE_FUTURE), "");
            addProperty(getRealTag(language, ALERT_DATE_PAST), "");
            addProperty(getRealTag(language, ALERT_BSN), "");
            addProperty(getRealTag(language, ALERT_IBAN), "");
            addProperty(getRealTag(language, ALERT_PHONE), "");
            addProperty(getRealTag(language, ALERT_POSTAL_CODE), "");
            addProperty(getRealTag(language, ALERT_DEFAULT), "");
        }

        addPropertiesSource(new PropertiesSourceFile(LANGUAGE_CONFIG_FOLDER + File.separator + LANGUAGE_CONFIG_FILE));
        readConfigurations();
    }

    @FindBugsSuppressWarnings("DC_DOUBLECHECK")
    public static OrbeonLanguageManager getInstance() {
        if (instance == null) {
            synchronized (WebformsConfigurationReader.class) {
                if (instance == null) {
                    instance = new OrbeonLanguageManager();
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

    public String getAlertDateFuture(OrbeonLanguage language) {
        return getPropertyLogException(getRealTag(language, ALERT_DATE_FUTURE));
    }

    public String getAlertDatePast(OrbeonLanguage language) {
        return getPropertyLogException(getRealTag(language, ALERT_DATE_PAST));
    }

    public String getAlertBsn(OrbeonLanguage language) {
        return getPropertyLogException(getRealTag(language, ALERT_BSN));
    }

    public String getAlertIban(OrbeonLanguage language) {
        return getPropertyLogException(getRealTag(language, ALERT_IBAN));
    }

    public String getAlertPhone(OrbeonLanguage language) {
        return getPropertyLogException(getRealTag(language, ALERT_PHONE));
    }

    public String getAlertPostalCode(OrbeonLanguage language) {
        return getPropertyLogException(getRealTag(language, ALERT_POSTAL_CODE));
    }

    public String getAlertDefault(OrbeonLanguage language) {
        return getPropertyLogException(getRealTag(language, ALERT_DEFAULT));
    }

    private String getRealTag(OrbeonLanguage language, String sufix) {
        return ORBEON_PREFIX + "." + language.getAbbreviature() + "." + sufix;
    }

    public String getAlertInvalidNumber(OrbeonLanguage language) {
        return getPropertyLogException(getRealTag(language, INVALID_NUMBER_CODE));
    }

}
