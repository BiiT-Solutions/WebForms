package com.biit.webforms.serialization;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.biit.form.json.serialization.TreeObjectDeserializer;
import com.biit.webforms.persistence.entity.DynamicAnswer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class DynamicAnswerDeserializer extends TreeObjectDeserializer<DynamicAnswer> {
	
	private HashMap<DynamicAnswer, List<String>> mapper;

	public DynamicAnswerDeserializer(HashMap<DynamicAnswer, List<String>> mapper) {
		super(DynamicAnswer.class);
		this.mapper = mapper;
	}

	@SuppressWarnings("unchecked")
	public void deserialize(JsonElement json, JsonDeserializationContext context, DynamicAnswer element) {
		JsonObject jobject = (JsonObject) json;
		
		super.deserialize(json, context, element);
		
		Type listType = new TypeToken<List<String>>() {}.getType();
		JsonElement pathsJson = jobject.get("reference");
		if (pathsJson != null) {
			List<String> path = new ArrayList<String>();
			path.addAll((List<String>)context.deserialize(pathsJson, listType));
			mapper.put(element, path);
		}
		
	}

	public HashMap<DynamicAnswer, List<String>> getMapper() {
		return mapper;
	}
		
}
