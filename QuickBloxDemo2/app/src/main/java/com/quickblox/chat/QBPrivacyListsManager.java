package com.quickblox.chat;

import com.quickblox.chat.JIDHelper;
import com.quickblox.chat.Manager;
import com.quickblox.chat.listeners.QBPrivacyListListener;
import com.quickblox.chat.model.QBPrivacyList;
import com.quickblox.chat.model.QBPrivacyListItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.privacy.PrivacyList;
import org.jivesoftware.smackx.privacy.PrivacyListListener;
import org.jivesoftware.smackx.privacy.PrivacyListManager;
import org.jivesoftware.smackx.privacy.packet.PrivacyItem;

public class QBPrivacyListsManager extends Manager {

   private static final Map INSTANCES = new WeakHashMap();
   private Set listeners = new CopyOnWriteArraySet();
   private PrivacyListManager privacyListManager;


   private QBPrivacyListsManager(XMPPConnection connection) {
      super(connection);
      this.privacyListManager = PrivacyListManager.getInstanceFor(connection);
      this.privacyListManager.addListener(new PrivacyListListener() {
         public void setPrivacyList(String s, List privacyItems) {
            ArrayList items = new ArrayList();
            Iterator i$ = privacyItems.iterator();

            while(i$.hasNext()) {
               PrivacyItem p = (PrivacyItem)i$.next();
               items.add(QBPrivacyListsManager.this.convertPrivacyListItemToQBPrivacyListItem(p));
            }

         }
         public void updatedPrivacyList(String s) {
            Iterator i$ = QBPrivacyListsManager.this.listeners.iterator();

            while(i$.hasNext()) {
               QBPrivacyListListener listener = (QBPrivacyListListener)i$.next();
               listener.updatedPrivacyList(s);
            }

         }
      });
   }

   static synchronized QBPrivacyListsManager getInstanceFor(XMPPConnection connection) {
      QBPrivacyListsManager manager = (QBPrivacyListsManager)INSTANCES.get(connection);
      if(manager == null) {
         manager = new QBPrivacyListsManager(connection);
      }

      return manager;
   }

   public void addPrivacyListListener(QBPrivacyListListener listener) {
      if(listener != null) {
         this.listeners.add(listener);
      }
   }

   public void removePrivacyListListener(QBPrivacyListListener listener) {
      this.listeners.remove(listener);
   }

   public Collection getPrivacyListListeners() {
      return Collections.unmodifiableCollection(this.listeners);
   }

   public List getPrivacyLists() throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
      List lists = this.privacyListManager.getPrivacyLists();
      ArrayList qbPrivacyLists = new ArrayList();
      Iterator i$ = lists.iterator();

      while(i$.hasNext()) {
         PrivacyList p = (PrivacyList)i$.next();
         qbPrivacyLists.add(this.convertPrivacyListToQBPrivacyList(p));
      }

      return qbPrivacyLists;
   }

   public QBPrivacyList getPrivacyList(String listName) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
      PrivacyList p = this.privacyListManager.getPrivacyList(listName);
      return this.convertPrivacyListToQBPrivacyList(p);
   }

   public void setPrivacyList(QBPrivacyList list) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException, IllegalArgumentException {
      if(list == null) {
         throw new IllegalArgumentException("QBPrivacyList instance is null");
      } else if(list.getItems() == null) {
         throw new IllegalArgumentException("QBPrivacyList.items is null");
      } else if(list.getItems().size() == 0) {
         throw new IllegalArgumentException("QBPrivacyList.items.size is 0");
      } else {
         ArrayList privacyItems = new ArrayList();

         for(int i = 0; i < list.getItems().size(); ++i) {
            QBPrivacyListItem qbItem = (QBPrivacyListItem)list.getItems().get(i);
            PrivacyItem item = this.convertQBPrivacyListItemToPrivacyListItem(qbItem, i + 1);
            privacyItems.add(item);
         }

         this.privacyListManager.createPrivacyList(list.getName(), privacyItems);
      }
   }

   public void deletePrivacyList(QBPrivacyList list) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
      this.deletePrivacyList(list.getName());
   }

   public void deletePrivacyList(String name) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
      this.privacyListManager.deletePrivacyList(name);
   }

   public void declineActiveList() throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
      this.privacyListManager.declineActiveList();
   }

   public void declineDefaultList() throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
      this.privacyListManager.declineDefaultList();
   }

   public void setPrivacyListAsActive(QBPrivacyList list) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
      this.setPrivacyListAsActive(list.getName());
   }

   public void setPrivacyListAsActive(String name) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
      this.privacyListManager.setActiveListName(name);
   }

   public void setPrivacyListAsDefault(QBPrivacyList list) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
      this.setPrivacyListAsDefault(list.getName());
   }

   public void setPrivacyListAsDefault(String name) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
      this.privacyListManager.setDefaultListName(name);
   }

   private QBPrivacyList convertPrivacyListToQBPrivacyList(PrivacyList list) {
      QBPrivacyList qblist = new QBPrivacyList();
      qblist.setName(list.getName());
      qblist.setActiveList(list.isActiveList());
      qblist.setDefaultList(list.isDefaultList());
      ArrayList items = new ArrayList();
      Iterator i$ = list.getItems().iterator();

      while(i$.hasNext()) {
         PrivacyItem item = (PrivacyItem)i$.next();
         items.add(this.convertPrivacyListItemToQBPrivacyListItem(item));
      }

      qblist.setItems(items);
      return qblist;
   }

   private QBPrivacyListItem convertPrivacyListItemToQBPrivacyListItem(PrivacyItem item) {
      QBPrivacyListItem qbItem = new QBPrivacyListItem();
      qbItem.setAllow(item.isAllow());
      switch(QBPrivacyListsManager.NamelessClass1184160891.$SwitchMap$org$jivesoftware$smackx$privacy$packet$PrivacyItem$Type[item.getType().ordinal()]) {
      case 1:
         qbItem.setType(this.getActualType(item));
         break;
      case 2:
         qbItem.setType(QBPrivacyListItem.Type.GROUP);
         break;
      case 3:
         qbItem.setType(QBPrivacyListItem.Type.SUBSCRIPTION);
      }

      qbItem.setValueForType(item.getValue());
      return qbItem;
   }

   private QBPrivacyListItem.Type getActualType(PrivacyItem item) {
      String value = item.getValue();
      return JIDHelper.INSTANCE.isMucJid(value)?QBPrivacyListItem.Type.GROUP_USER_ID:QBPrivacyListItem.Type.USER_ID;
   }

   private PrivacyItem convertQBPrivacyListItemToPrivacyListItem(QBPrivacyListItem qbitem, int order) {
      PrivacyItem.Type type;
      switch(QBPrivacyListsManager.NamelessClass1184160891.$SwitchMap$com$quickblox$chat$model$QBPrivacyListItem$Type[qbitem.getType().ordinal()]) {
      case 1:
      case 2:
         type = PrivacyItem.Type.jid;
         break;
      case 3:
         type = PrivacyItem.Type.group;
         break;
      case 4:
         type = PrivacyItem.Type.subscription;
         break;
      default:
         type = PrivacyItem.Type.jid;
      }

      PrivacyItem item = new PrivacyItem(type, qbitem.getValueForType(), qbitem.isAllow(), (long)order);
      item.setFilterIQ(true);
      item.setFilterMessage(true);
      item.setFilterPresenceIn(true);
      item.setFilterPresenceOut(true);
      return item;
   }


   // $FF: synthetic class
   static class NamelessClass1184160891 {

      // $FF: synthetic field
      static final int[] $SwitchMap$org$jivesoftware$smackx$privacy$packet$PrivacyItem$Type;
      // $FF: synthetic field
      static final int[] $SwitchMap$com$quickblox$chat$model$QBPrivacyListItem$Type = new int[QBPrivacyListItem.Type.values().length];


      static {
         try {
            $SwitchMap$com$quickblox$chat$model$QBPrivacyListItem$Type[QBPrivacyListItem.Type.USER_ID.ordinal()] = 1;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            $SwitchMap$com$quickblox$chat$model$QBPrivacyListItem$Type[QBPrivacyListItem.Type.GROUP_USER_ID.ordinal()] = 2;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$com$quickblox$chat$model$QBPrivacyListItem$Type[QBPrivacyListItem.Type.GROUP.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$com$quickblox$chat$model$QBPrivacyListItem$Type[QBPrivacyListItem.Type.SUBSCRIPTION.ordinal()] = 4;
         } catch (NoSuchFieldError var4) {
            ;
         }

         $SwitchMap$org$jivesoftware$smackx$privacy$packet$PrivacyItem$Type = new int[PrivacyItem.Type.values().length];

         try {
            $SwitchMap$org$jivesoftware$smackx$privacy$packet$PrivacyItem$Type[PrivacyItem.Type.jid.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$org$jivesoftware$smackx$privacy$packet$PrivacyItem$Type[PrivacyItem.Type.group.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$org$jivesoftware$smackx$privacy$packet$PrivacyItem$Type[PrivacyItem.Type.subscription.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
