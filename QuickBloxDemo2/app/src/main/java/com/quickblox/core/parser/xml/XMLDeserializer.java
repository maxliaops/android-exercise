package com.quickblox.core.parser.xml;

import com.quickblox.core.parser.xml.XMLHandler;
import com.quickblox.core.parser.xml.exception.QBXMLParserSyntaxException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLDeserializer {

   private Set deserializers = new HashSet();


   protected XMLDeserializer(XMLDeserializer.Builder builder) {
      this.deserializers = builder.deserializers;
   }

   public Object deserialize(Class deserializer, String xmlString) throws QBXMLParserSyntaxException {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = null;
      InputSource inputSource = new InputSource(new StringReader(xmlString));
      XMLHandler handler = new XMLHandler(deserializer);

      try {
         saxParser = factory.newSAXParser();
         saxParser.parse(inputSource, handler);
      } catch (ParserConfigurationException var8) {
         throw new QBXMLParserSyntaxException(var8.getLocalizedMessage());
      } catch (SAXException var9) {
         throw new QBXMLParserSyntaxException(var9.getLocalizedMessage());
      } catch (IOException var10) {
         throw new QBXMLParserSyntaxException(var10.getLocalizedMessage());
      }

      return handler.produceInstance();
   }

   public static class Builder {

      private Set deserializers = new HashSet();


      public XMLDeserializer.Builder registerDeserializer(Class deserializer) {
         this.deserializers.add(deserializer);
         return this;
      }

      public XMLDeserializer build() {
         return new XMLDeserializer(this);
      }
   }
}
