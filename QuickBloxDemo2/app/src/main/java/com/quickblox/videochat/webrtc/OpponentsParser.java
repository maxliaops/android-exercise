//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.videochat.webrtc;

import com.quickblox.chat.propertyparsers.MessagePropertyParser;
import com.quickblox.videochat.webrtc.QBSignalingSpec.QBSignalField;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class OpponentsParser implements MessagePropertyParser<List<Integer>> {
    public OpponentsParser() {
    }

    public XmlStringBuilder parseToXML(Object opponents) {
        XmlStringBuilder xmlOpponentBuilder = new XmlStringBuilder();
        xmlOpponentBuilder.openElement(QBSignalField.OPPONENTS.getValue());
        Iterator i$ = ((List)opponents).iterator();

        while(i$.hasNext()) {
            Integer opponentID = (Integer)i$.next();
            xmlOpponentBuilder.element(QBSignalField.OPPONENT.getValue(), String.valueOf(opponentID));
        }

        xmlOpponentBuilder.closeElement(QBSignalField.OPPONENTS.getValue());
        return xmlOpponentBuilder;
    }

    public List<Integer> parseFromXML(XmlPullParser parser) throws IllegalStateException {
        return this.parseOpponentsFromXML(parser);
    }

    private List<Integer> parseOpponentsFromXML(XmlPullParser parser) {
        ArrayList resultList = new ArrayList();
        String currentValue = null;
        String lastEndTag = "";

        try {
            while(!lastEndTag.equals(QBSignalField.OPPONENTS.getValue())) {
                int e = parser.getEventType();
                if(parser.getEventType() == 4) {
                    currentValue = parser.getText();
                } else if(parser.getEventType() == 3) {
                    if(currentValue != null) {
                        resultList.add(Integer.valueOf(currentValue));
                        currentValue = null;
                    }

                    lastEndTag = parser.getName();
                }

                if(!lastEndTag.equals(QBSignalField.OPPONENTS.getValue())) {
                    parser.next();
                }
            }
        } catch (IOException | XmlPullParserException var6) {
            var6.printStackTrace();
        }

        return resultList;
    }
}
