package com.biit.webforms.serialization;

import com.biit.form.json.serialization.StorableObjectSerializer;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Flow;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Type;

public class FlowSerializer extends StorableObjectSerializer<Flow> {

    @Override
    public JsonElement serialize(Flow src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        final JsonObject jsonObject = (JsonObject) super.serialize(src, typeOfSrc, context);

        if (src.getOrigin() != null) {
            jsonObject.add("originId", context.serialize(src.getOrigin().getPath()));
        } else {
            WebformsLogger.errorMessage(this.getClass().getName(), "Flow without originId!");
        }
        if (src.getOrigin() != null) {
            jsonObject.add("flowType", context.serialize(src.getFlowType()));
        } else {
            WebformsLogger.errorMessage(this.getClass().getName(), "Flow without flowtype!");
        }
        if (src.getDestiny() != null) {
            jsonObject.add("destinyId", context.serialize(src.getDestiny().getPath()));
        }
        jsonObject.add("others", context.serialize(src.isOthers()));
        //If is an others flow, getCondition returns the negation of the sum of all other conditions.
        //While the condition inner value is null.
        if (!src.isOthers()) {
            jsonObject.add("condition", context.serialize(src.getComputedCondition()));
        }

        return jsonObject;
    }

}
