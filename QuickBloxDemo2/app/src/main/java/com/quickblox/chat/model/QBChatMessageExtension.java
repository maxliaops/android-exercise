package com.quickblox.chat.model;

import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.propertyparsers.MessagePropertyParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class QBChatMessageExtension implements ExtensionElement {

   public static final String ELEMENT_NAME = "extraParams";
   public static final String NAMESPACE = "jabber:client";
   public static final String TAG_ATTACHMENT = "attachment";
   public static final String ATTRIBUTE_NAME = "name";
   public static final String ATTRIBUTE_TYPE = "type";
   public static final String ATTRIBUTE_ID = "id";
   public static final String ATTRIBUTE_URL = "url";
   public static final String ATTRIBUTE_CONTENT_TYPE = "content-type";
   public static final String ATTRIBUTE_SIZE = "size";
   private static Map complexPropertyParsers = new HashMap();
   private Map properties;
   private volatile Map complexPropertiesMap;
   private List attachments;


   QBChatMessageExtension() {
      this.properties = new HashMap();
      this.attachments = new ArrayList();
   }

   QBChatMessageExtension(Map properties, List attachments, Map complexProperties) {
      this.properties = properties;
      this.attachments = attachments;
      this.complexPropertiesMap = complexProperties;
   }

   public static void registerComplexPropertyParser(String elementName, MessagePropertyParser propertyParser) {
      complexPropertyParsers.put(elementName, propertyParser);
   }

   public String getElementName() {
      return "extraParams";
   }

   public String getNamespace() {
      return "jabber:client";
   }

   public CharSequence toXML() {
      XmlStringBuilder buf = new XmlStringBuilder();
      buf.halfOpenElement("extraParams").xmlnsAttribute("jabber:client").rightAngleBracket();
      Iterator i$ = this.attachments.iterator();

      while(i$.hasNext()) {
         QBAttachment key = (QBAttachment)i$.next();
         buf.halfOpenElement("attachment");
         buf.attribute("type", key.getType());
         if(key.getName() != null) {
            buf.attribute("name", key.getName());
         }

         if(key.getId() != null) {
            buf.attribute("id", String.valueOf(key.getId()));
         }

         if(key.getUrl() != null) {
            buf.attribute("url", key.getUrl());
         }

         if(key.getContentType() != null) {
            buf.attribute("content-type", key.getContentType());
         }

         if(key.getSize() != 0.0D) {
            buf.attribute("size", String.valueOf(key.getSize()));
         }

         buf.rightAngleBracket();
         buf.closeElement("attachment");
      }

      i$ = this.getPropertyNames().iterator();

      String key1;
      while(i$.hasNext()) {
         key1 = (String)i$.next();
         String currentObject = this.getProperty(key1);
         buf.element(key1, currentObject);
      }

      if(this.complexPropertiesMap != null) {
         i$ = this.complexPropertiesMap.keySet().iterator();

         while(i$.hasNext()) {
            key1 = (String)i$.next();
            Object currentObject1 = this.complexPropertiesMap.get(key1);
            MessagePropertyParser messagePropertyParser = (MessagePropertyParser)complexPropertyParsers.get(key1);
            if(messagePropertyParser != null) {
               buf.append(messagePropertyParser.parseToXML(currentObject1));
            }
         }
      }

      buf.closeElement("extraParams");
      return buf;
   }

   public void addAttachments(Collection attachments) {
      this.attachments.addAll(attachments);
   }

   public Collection getAttachments() {
      return Collections.unmodifiableList(this.attachments);
   }

   public String getProperty(String name) {
      return (String)this.properties.get(name);
   }

   public Object getComplexProperty(String name) {
      return this.complexPropertiesMap.get(name);
   }

   public void setProperty(String name, String value) {
      this.properties.put(name, value);
   }

   public void setComplexProperty(Map complexProperties) {
      this.complexPropertiesMap = complexProperties;
   }

   public void setProperties(Map properties) {
      this.properties.putAll(properties);
   }

   private Collection getPropertyNames() {
      return Collections.unmodifiableSet(new HashSet(this.properties.keySet()));
   }

   public Map getProperties() {
      return this.properties;
   }


   public static class Provider extends ExtensionElementProvider {

      private XmlPullParser parser;
      private List attachments;
      private Map properties;
      private Map complexProperties;


      public QBChatMessageExtension parse(XmlPullParser parser, int Depth) throws XmlPullParserException, IOException, SmackException {
         this.parser = parser;
         this.attachments = new ArrayList();
         this.properties = new HashMap();
         this.complexProperties = new HashMap();
         int tag = parser.next();

         while(this.isNotLastElement(tag)) {
            if(this.isWhitespaceText(tag)) {
               tag = parser.next();
            } else if(this.isAttachmentElement(tag)) {
               this.parseAttachment();
               tag = parser.next();
            } else if(this.isPropertyStartElement(tag)) {
               tag = this.parseProperty();
            } else if(this.isPropertyEndElement(tag)) {
               tag = parser.next();
            }
         }

         return new QBChatMessageExtension(this.properties, this.attachments, this.complexProperties);
      }

      private boolean isNotLastElement(int tag) {
         return !this.isLastElement(tag);
      }

      private boolean isLastElement(int tag) {
         return tag == 3 && this.parser.getName().equals("extraParams") || tag == 1;
      }

      private boolean isPropertyStartElement(int tag) {
         return tag == 2 && this.isNotRootElement();
      }

      private boolean isPropertyEndElement(int tag) {
         return tag == 3 && this.isNotRootElement();
      }

      private boolean isWhitespaceText(int tag) throws XmlPullParserException {
         return tag == 4 && this.parser.isWhitespace();
      }

      private boolean isNotRootElement() {
         return !this.parser.getName().equals("extraParams");
      }

      private boolean isAttachmentElement(int tag) {
         return tag == 2 && this.parser.getName().equals("attachment");
      }

      private void parseAttachment() {
         String attachmentId = this.parser.getAttributeValue((String)null, "id");
         String attachmentType = this.parser.getAttributeValue((String)null, "type");
         String attachmentUrl = this.parser.getAttributeValue((String)null, "url");
         QBAttachment attachment = new QBAttachment(attachmentType);
         if(attachmentId != null) {
            attachment.setId(attachmentId);
         }

         attachment.setUrl(attachmentUrl);
         this.attachments.add(attachment);
      }

      private int parseProperty() throws XmlPullParserException, IOException {
         String name = this.parser.getName();
         MessagePropertyParser complexParser = null;
         if((complexParser = (MessagePropertyParser)QBChatMessageExtension.complexPropertyParsers.get(name)) != null) {
            Object tag1 = this.parseComplexProperty(this.parser, complexParser);
            this.complexProperties.put(name, tag1);
            return this.parser.getEventType();
         } else {
            int tag = this.parser.next();
            if(tag == 4) {
               String value = this.parser.getText();
               this.properties.put(name, value);
               this.parser.next();
            } else {
               this.skipCurrentComplexTag(name);
            }

            return this.parser.getEventType();
         }
      }

      private void skipCurrentComplexTag(String closeTagName) {
         int tag = 0;
         String currentTagName = "";

         do {
            try {
               tag = this.parser.next();
               currentTagName = this.parser.getName();
            } catch (XmlPullParserException var5) {
               var5.printStackTrace();
            } catch (IOException var6) {
               var6.printStackTrace();
            }
         } while(3 != tag || !closeTagName.equals(currentTagName));

      }

      private Object parseComplexProperty(XmlPullParser parser, MessagePropertyParser propertyParser) {
         return propertyParser.parseFromXML(parser);
      }
   }
}
