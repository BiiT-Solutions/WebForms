package com.biit.webforms.serialization;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.condition.Token;
import com.biit.webforms.persistence.entity.condition.TokenBetween;
import com.biit.webforms.persistence.entity.condition.TokenComparationAnswer;
import com.biit.webforms.persistence.entity.condition.TokenComparationValue;
import com.biit.webforms.persistence.entity.condition.TokenIn;
import com.biit.webforms.persistence.entity.condition.TokenInValue;
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
	public void deserialize(JsonElement json,
			JsonDeserializationContext context, Form element) {
		JsonObject jobject = (JsonObject) json;

		try {
			element.setDescription(parseString("description", jobject, context));
		} catch (FieldTooLongException e) {
			throw new JsonParseException(e);
		}

		// Deserializes childs
		super.deserialize(json, context, element);

		// Deserializes Flows
		Type listType = new TypeToken<HashSet<Flow>>() {
		}.getType();
		JsonElement flowsJson = jobject.get("flows");
		if (flowsJson != null) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			//Redirects to proper deserializers.
			gsonBuilder.registerTypeAdapter(StorableObject.class, new StorableObjectDeserializer<StorableObject>());
			gsonBuilder.registerTypeAdapter(Flow.class, new FlowDeserializer(element));
			gsonBuilder.registerTypeAdapter(Token.class, new TokenDeserializer<Token>(Token.class));
			gsonBuilder.registerTypeAdapter(TokenBetween.class, new TokenBetweenDeserializer(element));
			gsonBuilder.registerTypeAdapter(TokenComparationAnswer.class, new TokenComparationAnswerDeserializer(element));
			gsonBuilder.registerTypeAdapter(TokenComparationValue.class, new TokenComparationValueDeserializer(element));
			gsonBuilder.registerTypeAdapter(TokenIn.class, new TokenInDeserializer(element));
			gsonBuilder.registerTypeAdapter(TokenInValue.class, new TokenInValueDeserializer(element));
			Gson gson = gsonBuilder.create();

			Set<Flow> flows = gson.fromJson(flowsJson, listType);
			
			element.addFlows(flows);
		}
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
