package com.max.contacts;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private static final String[] PHONES_PROJECTION = new String[] {
		Phone.CONTACT_ID, Phone.DISPLAY_NAME, Phone.NUMBER
	};
	private static final int PHONES_CONTACT_ID_INDEX = 0;
	private static final int PHONES_DISPLAY_NAME_INDEX = 1;
	private static final int PHONES_NUMBER_INDEX = 2;
	private static final String FILE_NAME = "contacts.txt";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button btnGetContacts = (Button)findViewById(R.id.btnGetContacts);
        btnGetContacts.setOnClickListener(mGetContactsListener);
    }
    
    OnClickListener mGetContactsListener = new OnClickListener() {
    	public void onClick(View v) {
    		getContacts();
    	}
    };
    
    private void getContacts() {
    	Long id;
    	String name = null;
    	String number = null;
    	FileOutputStream fos = null;
    	String tmp = null;
    	ContentResolver contentResolver = getContentResolver();
    	Cursor cursor = contentResolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
    	try {
			fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	tmp = "  ID            NAME          NUMBER\r\n";
    	try {
			fos.write(tmp.getBytes());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	if (cursor != null) {
    		while (cursor.moveToNext()) {
    			id = cursor.getLong(PHONES_CONTACT_ID_INDEX);
    			name = cursor.getString(PHONES_DISPLAY_NAME_INDEX);
    			number = cursor.getString(PHONES_NUMBER_INDEX);
    			tmp = String.format("%1$4d%2$16s%3$16s\r\n", id ,name, number);
    			try {
					fos.write(tmp.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    	try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}