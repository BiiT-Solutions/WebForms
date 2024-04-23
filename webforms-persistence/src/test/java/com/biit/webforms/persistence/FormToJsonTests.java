package com.biit.webforms.persistence;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.CompleteFormView;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;
import com.biit.webforms.persistence.entity.exceptions.BadFlowContentException;
import com.biit.webforms.persistence.entity.exceptions.FlowDestinyIsBeforeOriginException;
import com.biit.webforms.persistence.entity.exceptions.FlowNotAllowedException;
import com.biit.webforms.persistence.entity.exceptions.FlowSameOriginAndDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutDestinyException;
import com.biit.webforms.persistence.entity.exceptions.FlowWithoutSourceException;
import com.biit.webforms.persistence.entity.exceptions.InvalidAnswerSubformatException;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"jsonTests"})
public class FormToJsonTests {

    public void exportHiddenElementsInBlockToJson() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
            InvalidAnswerFormatException, InvalidAnswerSubformatException, ElementIsReadOnly {
        Block block = FormUtils.createBlock();
        // Hide some elements.
        TreeObject child = block.getChild("categoryInBlock1/questionInBlock1");
        child.setHiddenElement(true);

        // Block as json contains the hidden element.
        Assert.assertTrue(block.toJson().contains("\"label\" : \"questionInBlock1\","));
        Assert.assertTrue(block.toJson().contains("\"hidden\" : true,"));
    }

    public void exportHiddenElementsInFormToJson() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
            InvalidAnswerFormatException, InvalidAnswerSubformatException, BadFlowContentException, FlowWithoutSourceException,
            FlowSameOriginAndDestinyException, FlowDestinyIsBeforeOriginException, FlowWithoutDestinyException, NotValidTokenType, ElementIsReadOnly,
            FlowNotAllowedException {
        Block block = FormUtils.createBlock();
        // Hide some elements.
        TreeObject child = block.getChild("categoryInBlock1/questionInBlock1");
        child.setHiddenElement(true);

        Form form = FormUtils.createCompleteForm(block);
        Assert.assertTrue(form.getChild("categoryInBlock1/questionInBlock1").isHiddenElement());

        CompleteFormView completeFormView = new CompleteFormView(form);
        Assert.assertTrue(completeFormView.getChild("categoryInBlock1/questionInBlock1").isHiddenElement());

        // Form as json contains the hidden element.
        Assert.assertTrue(completeFormView.toJson().contains("\"label\" : \"questionInBlock1\","));
        Assert.assertTrue(completeFormView.toJson().contains("\"hidden\" : true,"));
    }
}
