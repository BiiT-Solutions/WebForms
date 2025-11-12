package com.biit.webforms.utils.math.domain;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
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

import com.biit.form.entity.BaseQuestion;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.utils.math.domain.exceptions.BadFormedExpressions;
import com.biit.webforms.utils.math.domain.exceptions.DifferentDateUnitForQuestions;
import com.biit.webforms.utils.math.domain.exceptions.IncompleteLogic;
import com.biit.webforms.utils.math.domain.exceptions.RedundantLogic;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

@Test(groups = {"testFlowDomainRedundant"})
public class TestFlowDomainRedundant {

    @Test(expectedExceptions = {RedundantLogic.class})
    public void testRedundantFlowIncorrect() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic, DifferentDateUnitForQuestions {
        Form form = loadForm("arie_redundant_flow" + File.separator + "arie_test_1.json");
        // Error
        new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category/Question3"));
    }

    @Test
    public void testRedundantFlowCorrect() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic, DifferentDateUnitForQuestions {
        Form form = loadForm("arie_redundant_flow" + File.separator + "arie_test_2.json");
        new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category/Question3"));
    }

    @Test
    public void testRedundantFlowCorrectOthers() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic, DifferentDateUnitForQuestions {
        Form form = loadForm("arie_redundant_flow" + File.separator + "arie_test_2_b.json");
        new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category/Question3"));
    }

    @Test
    public void testRedundantFlowCorrectOthersComplex() throws IOException, BadFormedExpressions, IncompleteLogic, RedundantLogic, DifferentDateUnitForQuestions {
        Form form = loadForm("arie_redundant_flow" + File.separator + "arie_test_3.json");
        new FlowUnitDomain(form, (BaseQuestion) form.getChild("Category/Question4"));
    }

    private Form loadForm(String filename) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream(filename), "UTF-8");

        return Form.fromJson(result);
    }
}
