//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.quickblox.chat;

import android.text.TextUtils;
import com.quickblox.chat.model.QBChatMessageExtension;
import com.quickblox.core.helper.CollectionUtils;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.filter.FlexibleStanzaTypeFilter;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;

class MessageAcknowledgeFilter extends FlexibleStanzaTypeFilter<Message> {
    MessageAcknowledgeFilter() {
    }

    protected boolean acceptSpecific(Message message) {
        if(!TextUtils.isEmpty(message.getBody())) {
            return true;
        } else {
            List extensions = message.getExtensions();
            Iterator i$ = extensions.iterator();

            ExtensionElement extension;
            do {
                if(!i$.hasNext()) {
                    return false;
                }

                extension = (ExtensionElement)i$.next();
            } while(!(extension instanceof QBChatMessageExtension) || CollectionUtils.isEmpty(((QBChatMessageExtension)extension).getAttachments()));

            return true;
        }
    }
}
