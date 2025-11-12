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

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.webforms.gui.tests.exceptions.FieldNotEditableException;
import com.biit.webforms.gui.tests.exceptions.IncorrectFileGenerationException;

public class XFormsTests extends WebFormsTester {

    private static final String NEW_FORM_NAME = "new_form_1";
    private static final String QUESTION1_NAME = "Question1";
    private static final String QUESTION2_NAME = "Question2";
    private static final String QUESTION3_NAME = "Question3";
    private static final String ANSWER1_NAME = "Q1Answer1";

    private static final Integer FIRST_ROW = 0;
    private static final Integer FIRST_COLUMN = 0;

    private static final String VALID_FLOW_TAG = "Valid";

	private void createFormElementsFlowAndFinishDesign() throws FieldNotEditableException {
			loginFormAdmin1();
			getFormManagerPage().createNewForm(NEW_FORM_NAME);
			// Create a couple of categories and questions
			goToDesignerPage();
			getDesignerPage().createAndSaveSimpleFormDesign();
			// Create a flow
			goToFlowManagerPage();
			getFlowManagerPage().createSimpleFlowRule(QUESTION1_NAME, QUESTION2_NAME);
			getFlowManagerPage().createOthersFlow(QUESTION1_NAME, QUESTION3_NAME);
			getFlowManagerPage().getFlowRulesTable().getCell(FIRST_ROW, FIRST_COLUMN).click();
			getFlowManagerPage().clickEditRuleButton();
			// Add the question IN [answer1 answer2] condition
			getFlowManagerPage().getFlowRuleWindow().searchForElement(QUESTION1_NAME);
			getFlowManagerPage().getFlowRuleWindow().selectElementAndNextElementInSubTreeTable(ANSWER1_NAME);
			getFlowManagerPage().getFlowRuleWindow().clickInButton();
			Assert.assertEquals(getFlowManagerPage().getFlowRuleWindow().getValidInvalidTagValue(), VALID_FLOW_TAG);
			getFlowManagerPage().getFlowRuleWindow().clickAccept();

			
			// Redraw the graph
			getFlowManagerPage().clickRedrawButton();
			getFlowManagerPage().saveFlow();
			goToFormManagerPage();
			goToDesignerPage();
			getDesignerPage().finishForm();
			clickAcceptButtonIfExists();
	}

    @Test(groups = "xForms")
    public void performImpactAnalysis() throws IncorrectFileGenerationException, FieldNotEditableException {
        createFormElementsFlowAndFinishDesign();
        getFormManagerPage().clickXFormsButton();
        getFormManagerPage().clickXFormsDownloadButton();
        getFormManagerPage().getDownloadWindow().checkCorrectFileGeneration();
        getFormManagerPage().getDownloadWindow().closeWindow();
        logOut();
        deleteForm();
    }
}
