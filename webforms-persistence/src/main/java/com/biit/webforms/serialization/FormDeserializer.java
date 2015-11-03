package com.biit.webforms.serialization;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.persistence.entity.Category;
import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.Group;
import com.biit.webforms.persistence.entity.Question;
import com.biit.webforms.persistence.entity.SystemField;
import com.biit.webforms.persistence.entity.Text;
import com.biit.webforms.persistence.entity.TreeObjectImage;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenBetween;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.persistence.entity.condition.TokenIn;
import com.biit.webforms.persistence.entity.condition.TokenInValue;
import com.biit.webforms.persistence.entity.webservices.WebserviceCall;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLink;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallInputLinkErrors;
import com.biit.webforms.persistence.entity.webservices.WebserviceCallOutputLink;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

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
		gsonBuilder.registerTypeAdapter(Group.class, new BaseRepeatableGroupDeserializer<Group>(Group.class));
		gsonBuilder.registerTypeAdapter(Question.class, new QuestionDeserializer());
		gsonBuilder.registerTypeAdapter(Text.class, new TextDeserializer());
		gsonBuilder.registerTypeAdapter(SystemField.class, new SystemFieldDeserializer());
		gsonBuilder.registerTypeAdapter(Answer.class, new AnswerDeserializer());
		gsonBuilder.registerTypeAdapter(DynamicAnswer.class, new DynamicAnswerDeserializer(mapper));
		gsonBuilder.registerTypeAdapter(Flow.class, new FlowDeserializer(element));
		gsonBuilder.registerTypeAdapter(Token.class, new TokenDeserializer<Token>(Token.class));
		gsonBuilder.registerTypeAdapter(TokenBetween.class, new TokenBetweenDeserializer(element));
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
			List<String> path = context.deserialize(pathsJson, listType);
			return form.getChild(path);
		}
		return null;
	}

}
