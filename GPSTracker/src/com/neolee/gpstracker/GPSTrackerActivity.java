package com.neolee.gpstracker;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GPSTrackerActivity extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ((Button) findViewById(R.id.button1)).setOnClickListener( 
                new View.OnClickListener(){ 

                        @Override 
                        public void onClick(View view) { 
                                // TODO Auto-generated method stub 
                               Intent intent = new Intent(GPSTrackerActivity.this, MainService.class);
                               startService(intent);
                        }  
                }); 

        ((Button) findViewById(R.id.button2)).setOnClickListener( 
                new View.OnClickListener(){ 

                        @Override 
                        public void onClick(View view) { 
                                // TODO Auto-generated method stub 
                               Intent intent = new Intent(GPSTrackerActivity.this, MainService.class);
                               stopService(intent);
                        } 
                });
    }
}