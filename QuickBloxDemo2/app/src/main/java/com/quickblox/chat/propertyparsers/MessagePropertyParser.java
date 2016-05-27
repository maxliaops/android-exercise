//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.chat.propertyparsers;

import org.jivesoftware.smack.util.XmlStringBuilder;
import org.xmlpull.v1.XmlPullParser;

public interface MessagePropertyParser<T> {
    XmlStringBuilder parseToXML(Object var1);

    T parseFromXML(XmlPullParser var1);
}
