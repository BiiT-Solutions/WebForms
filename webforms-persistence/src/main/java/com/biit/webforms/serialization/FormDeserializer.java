package com.biit.webforms.serialization;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.json.serialization.BaseFormDeserializer;
import com.biit.form.json.serialization.StorableObjectDeserializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.persistence.entity.*;
import com.biit.webforms.persistence.entity.condition.*;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLinkErrors;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

public class FormDeserializer extends BaseFormDeserializer<Form> {

    public FormDeserializer() {
        super(Form.class);
    }

    @Override
    public void deserialize(JsonElement json, JsonDeserializationContext context, Form element) {
        JsonObject jobject = (JsonObject) json;

        HashMap<DynamicAnswer, List<String>> mapper = new HashMap<DynamicAnswer, List<String>>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        // Redirects to proper deserializers.
        gsonBuilder.registerTypeAdapter(StorableObject.class, new StorableObjectDeserializer<StorableObject>());
        gsonBuilder.registerTypeAdapter(TreeObject.class, new StorableObjectDeserializer<TreeObject>());
        gsonBuilder.registerTypeAdapter(Category.class, new CategoryDeserializer());
        gsonBuilder.registerTypeAdapter(Group.class, new GroupDeserializer());
        gsonBuilder.registerTypeAdapter(Question.class, new QuestionDeserializer());
        gsonBuilder.registerTypeAdapter(Text.class, new TextDeserializer());
        gsonBuilder.registerTypeAdapter(AttachedFiles.class, new AttachedFilesDeserializer());
        gsonBuilder.registerTypeAdapter(SystemField.class, new SystemFieldDeserializer());
        gsonBuilder.registerTypeAdapter(Answer.class, new AnswerDeserializer());
        gsonBuilder.registerTypeAdapter(DynamicAnswer.class, new DynamicAnswerDeserializer(mapper));
        gsonBuilder.registerTypeAdapter(Flow.class, new FlowDeserializer(element));
        gsonBuilder.registerTypeAdapter(Token.class, new TokenDeserializer<Token>(Token.class));
        gsonBuilder.registerTypeAdapter(TokenBetween.class, new TokenBetweenDeserializer(element));
        gsonBuilder.registerTypeAdapter(TokenEmpty.class, new TokenEmptyDeserializer(element));
        gsonBuilder.registerTypeAdapter(TokenComparationAnswer.class, new TokenComparationAnswerDeserializer(element));
        gsonBuilder.registerTypeAdapter(TokenComparationValue.class, new TokenComparationValueDeserializer(element));
        gsonBuilder.registerTypeAdapter(TokenIn.class, new TokenInDeserializer(element));
        gsonBuilder.registerTypeAdapter(TokenInValue.class, new TokenInValueDeserializer(element));
        gsonBuilder.registerTypeAdapter(WebserviceCall.class, new WebserviceCallDeserializer(element));
        gsonBuilder.registerTypeAdapter(WebserviceCallInputLink.class, new WebserviceCallInputDeserializer(element));
        gsonBuilder.registerTypeAdapter(WebserviceCallInputLinkErrors.class, new WebserviceCallInputErrorsDeserializer());
        gsonBuilder.registerTypeAdapter(WebserviceCallOutputLink.class, new WebserviceCallOutputLinkDeserializer(element));
        gsonBuilder.registerTypeAdapter(TreeObjectImage.class, new TreeObjectImageDeserializer());
        Gson gson = gsonBuilder.create();

        element.setComparationId(parseString("comparationId", jobject, context));
        element.setCreationTime(parseTimestamp("creationTime", jobject, context));
        element.setUpdateTime(parseTimestamp("updateTime", jobject, context));
        element.setCreatedBy(parseLong("createdBy", jobject, context));
        element.setUpdatedBy(parseLong("updatedBy", jobject, context));
        element.setVersion(parseInteger("version", jobject, context));
        element.setOrganizationId(parseLong("organizationId", jobject, context));

        try {
            element.setLabel(parseString("label", jobject, context));
            element.setDescription(parseString("description", jobject, context));
        } catch (FieldTooLongException e) {
            throw new JsonParseException(e);
        }

        // Children deserialization
        Type listType = new TypeToken<ArrayList<TreeObject>>() {
        }.getType();
        JsonElement childrenJson = jobject.get("children");
        if (childrenJson != null) {
            List<TreeObject> children = gson.fromJson(childrenJson, listType);
            try {
                element.addChildren(children);
            } catch (NotValidChildException | ElementIsReadOnly e) {
                throw new JsonParseException(e);
            }
        }

        // Link dynamic answers
        for (DynamicAnswer dynamicAnswer : mapper.keySet()) {
            dynamicAnswer.setReference((Question) element.getChild(mapper.get(dynamicAnswer)));
        }

        // Deserializes Flows
        Type flowListType = new TypeToken<HashSet<Flow>>() {
        }.getType();
        JsonElement flowsJson = jobject.get("flows");

        if (flowsJson != null) {
            Set<Flow> flows = gson.fromJson(flowsJson, flowListType);
            element.addFlows(flows);
        }

        // Deserializes Webservice calls
        Type callListType = new TypeToken<HashSet<WebserviceCall>>() {
        }.getType();
        JsonElement callsJson = jobject.get("webserviceCalls");

        if (callsJson != null) {
            Set<WebserviceCall> calls = gson.fromJson(callsJson, callListType);
            element.addWebserviceCalls(calls);
        }

        element.setImage((TreeObjectImage) context.deserialize(jobject.get("image"), TreeObjectImage.class));
    }

    public static TreeObject parseTreeObjectPath(String name, Form form, JsonObject jobject, JsonDeserializationContext context) {
        // Deserializes reference by searching on the form.
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        JsonElement pathsJson = jobject.get(name);
        if (pathsJson != null) {
            try {
                List<String> path = context.deserialize(pathsJson, listType);
                return form.getChild(path);
            } catch (NullPointerException npe) {
                // Not in the json.
            }
        }
        return null;
    }

}
