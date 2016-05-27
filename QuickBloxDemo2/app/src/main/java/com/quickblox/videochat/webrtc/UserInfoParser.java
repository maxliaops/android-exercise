//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.videochat.webrtc;

import com.quickblox.chat.propertyparsers.MessagePropertyParser;
import com.quickblox.videochat.webrtc.QBSignalingSpec.QBSignalField;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class UserInfoParser implements MessagePropertyParser<Map<String, String>> {
    public UserInfoParser() {
    }

    public XmlStringBuilder parseToXML(Object userInfo) {
        XmlStringBuilder xmlUserInfoBuilder = new XmlStringBuilder();
        xmlUserInfoBuilder.openElement(QBSignalField.USER_INFO.getValue());
        Iterator i$ = ((Map)userInfo).keySet().iterator();

        while(i$.hasNext()) {
            String key = (String)i$.next();
            xmlUserInfoBuilder.element(key, (String)((Map)userInfo).get(key));
        }

        xmlUserInfoBuilder.closeElement(QBSignalField.USER_INFO.getValue());
        return xmlUserInfoBuilder;
    }

    public Map<String, String> parseFromXML(XmlPullParser parser) {
        return this.parseUserInfoXml(parser);
    }

    private Map<String, String> parseUserInfoXml(XmlPullParser parser) {
        HashMap result = new HashMap();
        String currentValue = null;
        String lastEndTag = "";

        try {
            while(!lastEndTag.equals(QBSignalField.USER_INFO.getValue())) {
                if(parser.getEventType() == 4) {
                    currentValue = parser.getText();
                } else if(parser.getEventType() == 3) {
                    lastEndTag = parser.getName();
                    if(currentValue != null) {
                        result.put(lastEndTag, currentValue);
                        currentValue = null;
                    }
                }

                if(!lastEndTag.equals(QBSignalField.OPPONENTS.getValue())) {
                    parser.next();
                }
            }
        } catch (IOException | XmlPullParserException var6) {
            var6.printStackTrace();
        }

        return result;
    }
}
