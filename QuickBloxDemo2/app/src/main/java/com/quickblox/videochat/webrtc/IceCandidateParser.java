//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.videochat.webrtc;

import com.quickblox.chat.propertyparsers.MessagePropertyParser;
import com.quickblox.videochat.webrtc.QBSignalingSpec.QBCandidate;
import com.quickblox.videochat.webrtc.QBSignalingSpec.QBSignalField;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.webrtc.IceCandidate;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class IceCandidateParser implements MessagePropertyParser<List<IceCandidate>> {
    private String xmlString;
    private HashMap<Integer, String> tags = new HashMap();
    private String lastStartTag;
    private String lastTagValue;
    private String lastSDPValue;
    private String lastSDPMidValue;
    private Integer lastSDPMIndexValue;

    public IceCandidateParser() {
    }

    public XmlStringBuilder parseToXML(Object candidates) {
        XmlStringBuilder xmlStringBuilder = new XmlStringBuilder();
        xmlStringBuilder.openElement(QBSignalField.CANDIDATES.getValue());
        Iterator i$ = ((List)candidates).iterator();

        while(i$.hasNext()) {
            IceCandidate candidate = (IceCandidate)i$.next();
            XmlStringBuilder xmlCandidate = new XmlStringBuilder();
            xmlStringBuilder.openElement(QBSignalField.CANDIDATE.getValue());
            xmlCandidate.element(QBCandidate.SDP_MLINE_INDEX.getValue(), String.valueOf(candidate.sdpMLineIndex));
            xmlCandidate.element(QBCandidate.SDP_MID.getValue(), String.valueOf(candidate.sdpMid));
            xmlCandidate.element(QBCandidate.CANDIDATE_DESC.getValue(), String.valueOf(candidate.sdp));
            xmlStringBuilder.append(xmlCandidate);
            xmlStringBuilder.closeElement(QBSignalField.CANDIDATE.getValue());
        }

        xmlStringBuilder.closeElement(QBSignalField.CANDIDATES.getValue());
        return xmlStringBuilder;
    }

    public List<IceCandidate> parseFromXML(XmlPullParser outParser) {
        return outParser != null?this.parseIceCandidatesXML(outParser):null;
    }

    private List<IceCandidate> parseIceCandidatesXML(XmlPullParser parser) {
        List result = null;

        try {
            result = this.processEventType(parser);
        } catch (IOException | XmlPullParserException var4) {
            var4.printStackTrace();
        }

        return result;
    }

    private List<IceCandidate> processEventType(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList iceCandidateList = new ArrayList();
        String lastEndTag = "";

        while(!lastEndTag.equals(QBSignalField.CANDIDATES.getValue())) {
            switch(parser.getEventType()) {
            case 2:
                this.lastStartTag = parser.getName();
                break;
            case 3:
                lastEndTag = parser.getName();
                if(lastEndTag.equalsIgnoreCase(QBCandidate.CANDIDATE_DESC.getValue())) {
                    this.lastSDPValue = this.lastTagValue;
                } else if(lastEndTag.equalsIgnoreCase(QBCandidate.SDP_MID.getValue())) {
                    this.lastSDPMidValue = this.lastTagValue;
                } else if(lastEndTag.equalsIgnoreCase(QBCandidate.SDP_MLINE_INDEX.getValue())) {
                    this.lastSDPMIndexValue = Integer.valueOf(this.lastTagValue);
                } else if(lastEndTag.equalsIgnoreCase(QBSignalField.CANDIDATE.getValue())) {
                    IceCandidate candidate = new IceCandidate(this.lastSDPMidValue, this.lastSDPMIndexValue.intValue(), this.lastSDPValue);
                    iceCandidateList.add(candidate);
                }
                break;
            case 4:
                this.lastTagValue = parser.getText();
            }

            if(!lastEndTag.equals(QBSignalField.CANDIDATES.getValue())) {
                parser.next();
            }
        }

        return iceCandidateList;
    }
}
