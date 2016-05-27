package com.quickblox.users.deserializer;

import com.qb.gson.JsonDeserializationContext;
import com.qb.gson.JsonDeserializer;
import com.qb.gson.JsonElement;
import com.qb.gson.JsonParseException;
import com.quickblox.core.helper.StringifyArrayList;
import java.lang.reflect.Type;
import java.util.Arrays;

public class QBStringifyArrayListDeserializer implements JsonDeserializer {

   public StringifyArrayList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      String str = json.toString();
      StringifyArrayList list = new StringifyArrayList(Arrays.asList(str.split(",")));
      return list;
   }
}
