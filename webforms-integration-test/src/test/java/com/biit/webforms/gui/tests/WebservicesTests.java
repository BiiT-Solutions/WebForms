package com.biit.webforms.gui.tests;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Test)
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

import com.biit.webforms.gui.tests.exceptions.FieldNotEditableException;
import org.testng.annotations.Test;

import java.time.LocalDateTime;

public class WebservicesTests extends WebFormsTester {

    private static final String NEW_FORM_NAME = "webservice form";
    private static final String NAME_BSN = "BSN";
    private static final String NAME_NAME = "name";
    private static final String NAME_SURNAME = "surname";
    private static final String NAME_ADDRESS = "address";
    private static final String WEBSERVICE_CALL_NAME = "bsn_call";
    private static final int WEBSERVICE_ROW = 2;

    @Test(groups = "webservices")
    public void generateFormWithWebservices() throws FieldNotEditableException {
        loginFormAdmin1();
        takeScreenshot(LocalDateTime.now() + "_before_delete_forms");
        getFormManagerPage().deleteAllCreatedForms();
        takeScreenshot(LocalDateTime.now() + "_after_delete_forms");

        getFormManagerPage().createNewForm(NEW_FORM_NAME);
        goToDesignerPage();

        getDesignerPage().addNewCategory();
        getDesignerPage().addNewInputTextSubformatBsnQuestion();
        getDesignerPage().getQuestionPropertiesView().setTechnicalName(NAME_BSN);

        getDesignerPage().addNewInputTextQuestion();
        getDesignerPage().getQuestionPropertiesView().setTechnicalName(NAME_NAME);
        getDesignerPage().addNewInputTextQuestion();
        getDesignerPage().getQuestionPropertiesView().setTechnicalName(NAME_SURNAME);
        getDesignerPage().addNewInputTextQuestion();
        getDesignerPage().getQuestionPropertiesView().setTechnicalName(NAME_ADDRESS);

        getDesignerPage().saveDesign();

        goToWebservicePage();
        waitFor(500);
        getWebservicePage().addWebserviceCall(WEBSERVICE_CALL_NAME, WEBSERVICE_ROW);
        getWebservicePage().save();

        getWebservicePage().editInputField(0, 2);
        getWebservicePage().editOutputField(0, 4, true);
        getWebservicePage().editOutputField(1, 2, false);
        getWebservicePage().editOutputField(2, 3, false);

        getWebservicePage().save();

        getWebservicePage().selectTrigger(3);
        getWebservicePage().save();

        logOut();
        deleteForm();
    }

    private void waitFor(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
