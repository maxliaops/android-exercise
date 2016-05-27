package com.quickblox.chat.propertyparsers;

import android.text.TextUtils;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ParsersUtils {

   public static XmlStringBuilder parceTagWithChild(XmlPullParser parser, String endTag, String mainTag) throws IOException, XmlPullParserException {
      String closeTag = null;
      LinkedList childNodes = new LinkedList();
      new LinkedList();
      XmlStringBuilder childTag = new XmlStringBuilder();
      String tagValue = "";

      while(!endTag.equals(closeTag)) {
         int tag = parser.getEventType();
         if(tag == 2) {
            loggingParserState(parser);
            parser.next();

            while(parser.getEventType() == 2) {
               childNodes.add(parceTagWithChild(parser, parser.getName(), mainTag));
               parser.next();
            }
         } else if(tag == 4) {
            tagValue = parser.getText();
            loggingParserState(parser);
            parser.next();
         } else if(tag == 3) {
            loggingParserState(parser);
            closeTag = parser.getName();
            if(endOfTag(tagValue)) {
               childTag.element(closeTag, tagValue);
            } else if(!endOfChildTags(mainTag, closeTag, tagValue)) {
               if(endOfMainTag(mainTag, closeTag, tagValue)) {
                  break;
               }
            } else {
               childTag.openElement(closeTag);
               Iterator i$ = childNodes.iterator();

               while(i$.hasNext()) {
                  XmlStringBuilder node = (XmlStringBuilder)i$.next();
                  childTag.append(node);
               }

               childTag.closeElement(closeTag);
               parser.next();
               if(!closeTag.equals(mainTag)) {
                  closeTag = null;
                  childNodes.clear();
               }
            }
         }
      }

      return childTag;
   }

   private static void loggingParserState(XmlPullParser parser) {
      StringBuilder stringBuilder = new StringBuilder();

      try {
         stringBuilder.append("Tag type is: ");
         stringBuilder.append(String.valueOf(parser.getEventType()));
         if(parser.getName() != null) {
            stringBuilder.append(",name is ");
            stringBuilder.append(parser.getName());
         } else {
            stringBuilder.append(", value is ");
            stringBuilder.append(parser.getText());
         }
      } catch (XmlPullParserException var3) {
         var3.printStackTrace();
      }

   }

   private static boolean endOfTag(String tagValue) {
      return !TextUtils.isEmpty(tagValue);
   }

   private static boolean endOfChildTags(String mainTag, String closeTag, String tagValue) {
      return TextUtils.isEmpty(tagValue) && !closeTag.equals(mainTag);
   }

   private static boolean endOfMainTag(String mainTag, String closeTag, String tagValue) {
      return TextUtils.isEmpty(tagValue) && closeTag.equals(mainTag);
   }
}
