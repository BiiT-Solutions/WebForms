package com.biit.webforms.persistence.entity;

import java.lang.reflect.Type;

import com.biit.webforms.serialization.StorableObjectSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class FlowSerializer extends StorableObjectSerializer<Flow>{

	@Override
	public JsonElement serialize(Flow src, Type typeOfSrc,
			JsonSerializationContext context) {
		final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);
		
		jsonObject.add("origin_id", context.serialize(src.getOrigin().getPath()));
		jsonObject.add("flowType", context.serialize(src.getFlowType()));
		if(src.getDestiny()!=null){
			jsonObject.add("destiny_id", context.serialize(src.getDestiny().getPath()));
		}
		jsonObject.add("others", context.serialize(src.isOthers()));
		//If is a others flow, getCondition returns the negation of the sum of all other conditions.
		//While the condition inner value is null.
		if(!src.isOthers()){
			jsonObject.add("condition", context.serialize(src.getComputedCondition()));
		}

		return jsonObject;
	}
	
}
