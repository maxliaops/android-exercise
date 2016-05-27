package com.quickblox.videochat.webrtc;

import com.quickblox.videochat.webrtc.IceCandidateParser;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.webrtc.IceCandidate;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

class IceCandidateParserTest extends TestCase {

   private List testCandidateList;
   private IceCandidateParser parser;
   private String checkString;
   private XmlStringBuilder checkXML;


   protected void setUp() throws Exception {
      super.setUp();
      this.parser = new IceCandidateParser();
      this.testCandidateList = new ArrayList();

      for(int i = 0; i < 2; ++i) {
         this.testCandidateList.add(new IceCandidate("sdpMid", i, "sdp"));
      }

      this.checkString = "<iceCandidates><iceCandidate><sdpMLineIndex>0</sdpMLineIndex><sdpMid>sdpMid</sdpMid><candidate>sdp</candidate></iceCandidate><iceCandidate><sdpMLineIndex>1</sdpMLineIndex><sdpMid>sdpMid</sdpMid><candidate>sdp</candidate></iceCandidate></iceCandidates>";
   }

   public void testParseToXML() {
      XmlStringBuilder resultXML = this.parser.parseToXML(this.testCandidateList);
      assertTrue(resultXML.toString().equals(this.checkString));
   }

   public void testParseFromXML() {
      XmlPullParser testParser = null;

      try {
         XmlPullParserFactory resultCandidates = XmlPullParserFactory.newInstance();
         resultCandidates.setNamespaceAware(true);
         testParser = resultCandidates.newPullParser();
         testParser.setInput(new StringReader(this.checkString));
      } catch (XmlPullParserException var3) {
         var3.printStackTrace();
      }

      List resultCandidates1 = this.parser.parseFromXML(testParser);
      assertTrue(this.isIceCandidatesEquals(resultCandidates1));
   }

   private boolean isIceCandidatesEquals(List resultCandidates) {
      if(resultCandidates.size() != this.testCandidateList.size()) {
         return false;
      } else {
         int iceCandidatesCoincidence = 0;
         Iterator i$ = this.testCandidateList.iterator();

         while(i$.hasNext()) {
            IceCandidate candidate = (IceCandidate)i$.next();
            Iterator i$1 = resultCandidates.iterator();

            while(i$1.hasNext()) {
               IceCandidate resCandidate = (IceCandidate)i$1.next();
               if(candidate.sdp.equals(resCandidate.sdp) && candidate.sdpMid.equals(resCandidate.sdpMid) && candidate.sdpMLineIndex == resCandidate.sdpMLineIndex) {
                  ++iceCandidatesCoincidence;
                  break;
               }
            }
         }

         if(iceCandidatesCoincidence == resultCandidates.size()) {
            return true;
         } else {
            return false;
         }
      }
   }
}
