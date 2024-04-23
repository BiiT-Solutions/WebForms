package com.biit.webforms.serialization;

import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.form.jackson.serialization.BaseFormDeserializer;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenIn;
import com.biit.webforms.persistence.entity.condition.TokenWithQuestion;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class FormElementDeserializer<T extends Form> extends BaseFormDeserializer<T> {


    @Override
    public void deserialize(T element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setId(parseLong("id", jsonObject));
        element.setComparationId(parseString("comparationId", jsonObject));
        element.setCreationTime(parseTimestamp("creationTime", jsonObject));
        element.setUpdateTime(parseTimestamp("updateTime", jsonObject));
        element.setCreatedBy(parseLong("createdBy", jsonObject));
        element.setUpdatedBy(parseLong("updatedBy", jsonObject));
        element.setVersion(parseInteger("version", jsonObject));
        element.setOrganizationId(parseLong("organizationId", jsonObject));
        element.setStatus(FormWorkStatus.from(parseString("status", jsonObject)));
        element.setLinkedFormLabel(parseString("linkedFormLabel", jsonObject));
        element.setLinkedFormOrganizationId(parseLong("linkedFormOrganizationId", jsonObject));
        element.setFormReferenceId(parseLong("formReferenceId", jsonObject));

        try {
            element.setLabel(parseString("label", jsonObject));
            element.setDescription(parseString("description", jsonObject));
        } catch (FieldTooLongException e) {
            throw new JsonGenerationException(e, null);
        }

        if ((jsonObject.get("image") != null)) {
            element.setImage(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("image").toString(), TreeObjectImage.class));
        }


        //LinkedFormVersions
        if ((jsonObject.get("linkedFormVersions") != null)) {
            element.setLinkedFormVersions(new HashSet<>(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("linkedFormVersions").toString(), Integer[].class))));
        }

        // Deserializes elementsToHide
        if ((jsonObject.get("elementsToHide") != null)) {
            final Set<Long> toHide = new HashSet<>(Arrays.asList(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("elementsToHide").toString(), Long[].class)));
            element.setElementsToHideId(toHide);
            if (!toHide.isEmpty()) {
                final Set<TreeObject> childrenToHide = new HashSet<>();
                toHide.forEach(id -> {
                    Optional<TreeObject> elements = element.getChildren().stream().filter(treeObject -> Objects.equals(treeObject.getId(), id)).findAny();
                    elements.ifPresent(childrenToHide::add);
                });
                element.setElementsToHide(childrenToHide);
            }
        }

        // Link dynamic answers
        element.getAllChildrenInHierarchy().forEach(children -> {
            if (children instanceof DynamicAnswer) {
                update(element, (DynamicAnswer) children);
            }
        });

        // Deserializes Flows
        if ((jsonObject.get("flows") != null)) {
            final Set<Flow> flows = new HashSet<>(Arrays.asList(ObjectMapperFactory.getObjectMapper()
                    .readValue(jsonObject.get("flows").toString(), Flow[].class)));
            element.addFlows(flows);
            //Fixing token references.
            flows.forEach(flow -> update(element, flow));
        }

        // Deserializes Webservice calls
        if ((jsonObject.get("webserviceCalls") != null)) {
            final Set<WebserviceCall> webserviceCalls = new HashSet<>(Arrays.asList(ObjectMapperFactory.getObjectMapper()
                    .readValue(jsonObject.get("webserviceCalls").toString(), WebserviceCall[].class)));
            element.addWebserviceCalls(webserviceCalls);
            //Fixing token references.
            webserviceCalls.forEach(webserviceCall -> update(element, webserviceCall));
        }
    }

    private void update(Form form, WebserviceCall webserviceCall) {
        webserviceCall.setFormElementTrigger((BaseQuestion) form.getChild(webserviceCall.getFormElementTriggerPath()));
        webserviceCall.getInputLinks().forEach(webserviceCallInputLink -> {
            webserviceCallInputLink.setFormElement((BaseQuestion) form.getChild(webserviceCallInputLink.getFormElementPath()));
            webserviceCallInputLink.setWebserviceCall(webserviceCall);
            webserviceCallInputLink.getErrors().forEach(webserviceCallInputLinkErrors -> {
                webserviceCallInputLinkErrors.setWebserviceCallInputLink(webserviceCallInputLink);
            });
        });
        webserviceCall.getOutputLinks().forEach(webserviceCallOutputLink -> {
            webserviceCallOutputLink.setFormElement((BaseQuestion) form.getChild(webserviceCallOutputLink.getFormElementPath()));
            webserviceCallOutputLink.setWebserviceCall(webserviceCall);
        });
    }

    private void update(Form form, DynamicAnswer dynamicAnswer) {
        dynamicAnswer.setReference((Question) form.getChild(dynamicAnswer.getReferencePath()));
    }

    private void update(Form form, Flow flow) {
        flow.setOrigin((Question) form.getChild(flow.getOriginReferencePath()));
        flow.setDestiny((Question) form.getChild(flow.getDestinyReferencePath()));
        flow.setForm(form);

        //Update conditions
        if (flow.getCondition() != null) {
            flow.getCondition().forEach(token -> {
                updateTokenReferences(form, token);
            });
        }
    }

    private void updateTokenReferences(Form form, Token token) {
        if (token instanceof TokenWithQuestion) {
            ((TokenWithQuestion) token).setQuestion((Question) form.getChild(((TokenWithQuestion) token).getQuestionReferencePath()));
        }
        if (token instanceof TokenComparationAnswer) {
            ((TokenComparationAnswer) token).setAnswer((Answer) form.getChild(((TokenComparationAnswer) token).getAnswerReferencePath()));
        }
        if (token instanceof TokenIn) {
            ((TokenIn) token).getValues().forEach(tokenInValue ->
                    tokenInValue.setAnswerValue((Answer) form.getChild((tokenInValue.getAnswerReferencePath()))));
        }
    }


}
