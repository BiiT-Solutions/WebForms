package com.biit.webforms.serialization;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.biit.form.entity.BaseQuestion;
import com.biit.persistence.entity.StorableObject;
import com.biit.webforms.enumerations.FlowType;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.condition.Token;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class FlowDeserializer extends StorableObjectDeserializer<Flow> {

	private final Form form;

	@Override
	public void deserialize(JsonElement json, JsonDeserializationContext context, Flow element) {
		JsonObject jobject = (JsonObject) json;

		element.setOrigin((BaseQuestion) FormDeserializer.parseTreeObjectPath("origin_id", form, jobject, context));
		element.setFlowType(parseFlowType("flowType", jobject, context));
		element.setDestiny((BaseQuestion) FormDeserializer.parseTreeObjectPath("destiny_id", form, jobject, context));
		element.setOthers(parseBoolean("others", jobject, context));

		if (!element.isOthers()) {
			element.setCondition(parseCondition("condition", jobject, context));
		}

		super.deserialize(json, context, element);
	}

	protected List<Token> parseCondition(String name, JsonObject jobject, JsonDeserializationContext context) {
		List<Token> condition = new ArrayList<Token>();

		JsonElement valuesJson = jobject.get(name);
		if (valuesJson != null) {
			Type listType = new TypeToken<List<StorableObject>>() {
			}.getType();
			@SuppressWarnings("unchecked")
			List<StorableObject> tokens = (List<StorableObject>) context.deserialize(valuesJson, listType);
			if (tokens != null) {
				for (StorableObject token : tokens) {
					condition.add((Token) token);
				}
			}
		}

		return condition;
	}

	public static FlowType parseFlowType(String name, JsonObject jobject, JsonDeserializationContext context) {
		if (jobject.get(name) != null) {
			return (FlowType) context.deserialize(jobject.get(name), FlowType.class);
		}
		return null;
	}

	public FlowDeserializer(Form element) {
		this.form = element;
	}

	@Override
	public Flow deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		Flow instance = new Flow();
		deserialize(json, context, instance);
		return instance;
	}
}
