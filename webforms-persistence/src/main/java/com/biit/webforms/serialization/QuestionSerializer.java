package com.biit.webforms.serialization;

import java.lang.reflect.Type;

import com.biit.form.json.serialization.TreeObjectSerializer;
import com.biit.webforms.persistence.entity.Question;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class QuestionSerializer extends TreeObjectSerializer<Question> {

	@Override
	public JsonElement serialize(Question src, Type typeOfSrc, JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);

		jsonObject.add("abbreviation", context.serialize(src.getAbbreviation()));
		jsonObject.add("alias", context.serialize(src.getAlias()));
		jsonObject.add("answerType", context.serialize(src.getAnswerType()));
		jsonObject.add("answerFormat", context.serialize(src.getAnswerFormat()));
		jsonObject.add("answerSubformat", context.serialize(src.getAnswerSubformat()));
		jsonObject.add("mandatory", context.serialize(src.isMandatory()));
		jsonObject.add("horizontal", context.serialize(src.isHorizontal()));
		jsonObject.add("description", context.serialize(src.getDescription()));
		jsonObject.add("defaultValueString", context.serialize(src.getDefaultValueString()));
		if (src.getDefaultValueAnswer() != null) {
			jsonObject.add("defaultValueAnswer", context.serialize(src.getDefaultValueAnswer().getValue()));
		} else {
			jsonObject.add("defaultValueAnswer", context.serialize(null));
		}
		jsonObject.add("defaultValueTime", context.serialize(src.getDefaultValueTime()));
		if (src.getImage() != null) {
			jsonObject.add("image", context.serialize(src.getImage()));
		} else {
			jsonObject.add("image", context.serialize(null));
		}

		return jsonObject;
	}

}
