package com.quickblox.core.parser.xml;

import com.quickblox.core.parser.xml.annotations.Element;
import com.quickblox.core.parser.xml.annotations.Root;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {

   private Class clazz;
   private Object instance;
   private String currentTag;
   private StringBuilder stringBuilder;
   private boolean checkedRootElement;


   public XMLHandler(Class clazz) {
      this.clazz = clazz;
   }

   public void startDocument() throws SAXException {
      super.startDocument();
      boolean errorExist = false;

      try {
         this.instance = this.clazz.newInstance();
         this.stringBuilder = new StringBuilder();
      } catch (InstantiationException var3) {
         errorExist = true;
      } catch (IllegalAccessException var4) {
         errorExist = true;
      }

      if(errorExist) {
         throw new SAXException("Couldn\'t initialize XML handler!");
      }
   }

   public void endDocument() throws SAXException {
      super.endDocument();
   }

   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      super.startElement(uri, localName, qName, attributes);
      if(!this.checkedRootElement) {
         this.checkedRootElement = this.isPresentRootElement(qName, this.clazz);
         if(!this.checkedRootElement) {
            throw new SAXException("Root element " + qName + " wasn\'t defined");
         }
      } else {
         this.currentTag = qName;
      }
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      super.characters(ch, start, length);
      if(this.currentTag != null) {
         char[] chars = new char[length];
         System.arraycopy(ch, start, chars, 0, length);
         this.stringBuilder.append(chars);
      }
   }

   public void endElement(String uri, String localName, String qName) throws SAXException {
      super.endElement(uri, localName, qName);
      if(qName.equals(this.currentTag)) {
         Field field = this.findFieldForElement(this.currentTag, this.clazz);
         if(field != null) {
            this.setFieldValue(field, this.stringBuilder.toString());
         }

         this.stringBuilder.setLength(0);
         this.currentTag = null;
      }

   }

   private void setFieldValue(Field field, String value) {
      value = value.replace("\n", "").replace("\"", "").trim();
      field.setAccessible(true);

      try {
         field.set(this.instance, value);
      } catch (IllegalAccessException var4) {
         var4.printStackTrace();
      }

   }

   private Field findFieldForElement(String fieldName, Class clazz) {
      Object returnField = null;
      Field[] fields = clazz.getDeclaredFields();
      Field[] arr$ = fields;
      int len$ = fields.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Field field = arr$[i$];
         if(field.isAnnotationPresent(Element.class)) {
            Element elementAnnotation = (Element)field.getAnnotation(Element.class);
            if(elementAnnotation.name().equals(fieldName)) {
               return field;
            }
         }
      }

      return (Field)returnField;
   }

   private boolean isPresentRootElement(String rootName, Class clazz) {
      boolean result = false;
      if(clazz.isAnnotationPresent(Root.class)) {
         Annotation typeAnnotation = clazz.getAnnotation(Root.class);
         result = ((Root)typeAnnotation).name().equals(rootName);
      }

      return result;
   }

   public Object produceInstance() {
      return this.instance;
   }
}
