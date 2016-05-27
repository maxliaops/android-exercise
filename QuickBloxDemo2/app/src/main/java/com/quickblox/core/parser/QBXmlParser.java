package com.quickblox.core.parser;

import android.os.Bundle;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.interfaces.QBErrorParser;
import com.quickblox.core.parser.QBResponseParser;
import com.quickblox.core.parser.xml.XMLDeserializer;
import com.quickblox.core.parser.xml.exception.QBXMLParserSyntaxException;
import com.quickblox.core.rest.RestResponse;
import com.quickblox.core.result.Result;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class QBXmlParser implements QBResponseParser {

   protected Class deserializer;
   protected AtomicBoolean canceled = new AtomicBoolean(false);
   protected List defaultParserErrors = Arrays.asList(new String[]{"Incorrect content type"});
   private QBErrorParser errorParser;


   public final Object parse(RestResponse restResponse, Bundle bundle) throws QBResponseException {
      Result restResult = new Result();
      if(this.errorParser != null) {
         restResult.setErrorParser(this.errorParser);
      }

      restResult.setResponse(restResponse);
      if(!restResult.isSuccess()) {
         throw new QBResponseException(restResult.getErrors());
      } else {
         Object responseInternal = this.parseResponseInternal(restResponse, bundle);
         return this.extractEntity(responseInternal);
      }
   }

   public void cancel() {
      this.canceled.set(true);
   }

   public void setDeserializer(Class deserializer) {
      this.deserializer = deserializer;
   }

   protected void setErrorParser(QBErrorParser errorParser) {
      this.errorParser = errorParser;
   }

   protected Object parseResponseInternal(RestResponse restResponse, Bundle bundle) throws QBResponseException {
      XMLDeserializer.Builder builder = new XMLDeserializer.Builder();
      builder.registerDeserializer(this.deserializer);
      XMLDeserializer xmlDeserializer = builder.build();

      try {
         return xmlDeserializer.deserialize(this.deserializer, restResponse.getRawBody());
      } catch (QBXMLParserSyntaxException var6) {
         throw new QBResponseException(this.defaultParserErrors);
      }
   }

   protected void setParserErrors(List errors) {
      if(errors != null) {
         this.defaultParserErrors = errors;
      }

   }

   protected Object extractEntity(Object response) {
      return response;
   }
}
