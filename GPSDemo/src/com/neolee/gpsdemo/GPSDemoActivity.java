package com.neolee.gpsdemo;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class GPSDemoActivity extends Activity {
	LocationManager locationManager;
	LocationListener locationListener;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
    
    private class MyLocationListener implements LocationListener {

    	@Override
    	public void onLocationChanged(Location location) {
    		// TODO Auto-generated method stub
    		String latLongString;
    		
    		if (location != null) {
            	double lat = location.getLatitude();
            	double lng = location.getLongitude();
            
            	latLongString = "纬度:" + lat + "/n经度:" + lng;
            } else {
            	latLongString = "无法获取地理信息";
            }
            
            Toast.makeText(getBaseContext(), latLongString, Toast.LENGTH_SHORT).show();
    	}

    	@Override
    	public void onProviderDisabled(String provider) {
    		// TODO Auto-generated method stub

    	}

    	@Override
    	public void onProviderEnabled(String provider) {
    		// TODO Auto-generated method stub

    	}

    	@Override
    	public void onStatusChanged(String provider, int status, Bundle extras) {
    		// TODO Auto-generated method stub

    	}

    }
}