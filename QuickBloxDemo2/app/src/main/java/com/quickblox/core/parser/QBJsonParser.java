package com.quickblox.core.parser;

import android.os.Bundle;
import android.text.TextUtils;
import com.qb.gson.Gson;
import com.qb.gson.GsonBuilder;
import com.qb.gson.JsonDeserializer;
import com.qb.gson.JsonParseException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.model.QBEntityWrap;
import com.quickblox.core.parser.QBJsonErrorParser;
import com.quickblox.core.parser.QBResponseParser;
import com.quickblox.core.query.JsonQuery;
import com.quickblox.core.rest.RestResponse;
import com.quickblox.core.result.Result;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class QBJsonParser implements QBResponseParser {

   private Map typesAdapterForDeserializer;
   protected boolean isCancel = false;
   protected Type deserializer;
   protected JsonDeserializer typeAdapterForDeserializer;
   protected Type typeForDeserializer;
   private Bundle additionalResult = new Bundle();
   private JsonQuery query;


   public QBJsonParser(JsonQuery query) {
      this.query = query;
   }

   public void setDeserializer(Type deserializer) {
      this.deserializer = deserializer;
   }

   public void setTypeForDeserializer(Type type) {
      this.typeForDeserializer = type;
   }

   public void setTypeAdapterForDeserializer(JsonDeserializer typeAdapterForDeserializer) {
      this.typeAdapterForDeserializer = typeAdapterForDeserializer;
   }

   public void putJsonTypeAdapter(Type type, Object typeAdapter) {
      if(this.typesAdapterForDeserializer == null) {
         this.typesAdapterForDeserializer = new HashMap();
      }

      this.typesAdapterForDeserializer.put(type, typeAdapter);
   }

   public void initParser(Type deserializer, Type typeForDeserializer, JsonDeserializer typeAdapterForDeserializer) {
      this.setDeserializer(deserializer);
      if(typeForDeserializer == null) {
         typeForDeserializer = deserializer;
      }

      this.putJsonTypeAdapter(typeForDeserializer, typeAdapterForDeserializer);
   }

   protected Bundle getBundle() {
      return this.additionalResult;
   }

   public Object parse(RestResponse restResponse, Bundle bundle) throws QBResponseException {
      this.setBundle(bundle);
      Result restResult = new Result();
      restResult.setErrorParser(new QBJsonErrorParser());
      restResult.setQuery(this.query);
      restResult.setResponse(restResponse);
      if(!restResult.isSuccess()) {
         throw new QBResponseException(restResult.getStatusCode(), restResult.getErrors());
      } else {
         Object parsedJsonResponse = this.parseJsonResponse(restResponse, restResult);
         return this.extractEntity(parsedJsonResponse);
      }
   }

   private void setBundle(Bundle bundle) {
      this.additionalResult = bundle;
   }

   protected Object parseJsonResponse(RestResponse response, Result result) throws QBResponseException {
      Object parsedObj = null;
      String rawBody = response.getRawBody();
      if(this.deserializer != null && !TextUtils.isEmpty(rawBody)) {
         GsonBuilder gsonBuilder = new GsonBuilder();
         if(!this.buildGsonFromMap(gsonBuilder) && this.typeAdapterForDeserializer != null) {
            Type gson = this.typeForDeserializer != null?this.typeForDeserializer:this.deserializer;
            gsonBuilder.registerTypeAdapter(gson, this.typeAdapterForDeserializer);
         }

         Gson gson1 = gsonBuilder.create();

         try {
            parsedObj = gson1.fromJson(rawBody, this.deserializer);
         } catch (JsonParseException var8) {
            throw new QBResponseException(var8.getLocalizedMessage());
         }
      }

      return parsedObj;
   }

   private boolean buildGsonFromMap(GsonBuilder gsonBuilder) {
      boolean result;
      if(result = this.typesAdapterForDeserializer != null && !this.typesAdapterForDeserializer.isEmpty()) {
         Set entries = this.typesAdapterForDeserializer.entrySet();
         Iterator i$ = entries.iterator();

         while(i$.hasNext()) {
            Entry typeAdapter = (Entry)i$.next();
            gsonBuilder.registerTypeAdapter((Type)typeAdapter.getKey(), typeAdapter.getValue());
         }
      }

      return result;
   }

   protected Object extractEntity(Object parsedEntity) {
      if(parsedEntity != null && parsedEntity instanceof QBEntityWrap) {
         parsedEntity = ((QBEntityWrap)parsedEntity).getEntity();
      }

      return parsedEntity;
   }

   public void cancel() {
      this.isCancel = true;
   }
}
