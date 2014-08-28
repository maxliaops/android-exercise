package org.liaops.tracking;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class Main extends Activity {
	int mUserID;
	BMapManager mBMapMan = null;
	MapView mMapView = null;
	MapController mMapController = null;
	MKMapViewListener mMapListener = null;
	LocationClient mLocClient;
	MyLocationListenner myListener = new MyLocationListenner();
	Button testUpdateButton = null;
	MyLocationOverlay myLocationOverlay = null;
	LocationData locData = null;
	LocationData locView = null;
	ArrayList<LocationData> mLocations = new ArrayList<LocationData>();
	               
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	Intent login_intent = getIntent();
    	mUserID = login_intent.getIntExtra("id_user", 0);
    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
		
    	
    	mBMapMan=new BMapManager(getApplication());
    	mBMapMan.init("4AC7DE5874512F482A9012948BB6DF9FF9206823", null);  
    	//注意：请在试用setContentView前初始化BMapManager对象，否则会报错
    	setContentView(R.layout.main);
    	mMapView=(MapView)findViewById(R.id.bmapView);
    	//mMapView.setBuiltInZoomControls(true);
    	//设置启用内置的缩放控件
    	mMapController=mMapView.getController();
    	mMapView.setLongClickable(true);
    	

        mLocClient = new LocationClient( this );
        mLocClient.registerLocationListener( myListener );
        
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");     //设置坐标类型
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        
        mMapView.setBuiltInZoomControls(true);
        mMapListener = new MKMapViewListener() {
            
            @Override
            public void onMapMoveFinish() {
                // TODO Auto-generated method stub
            }
            
            @Override
            public void onClickMapPoi(MapPoi mapPoiInfo) {
                // TODO Auto-generated method stub
                String title = "";
                if (mapPoiInfo != null){
                    title = mapPoiInfo.strText;
                    Toast.makeText(Main.this,title,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetCurrentMap(Bitmap b) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void onMapAnimationFinish() {
                // TODO Auto-generated method stub
                
            }
        };
        mMapView.regMapViewListener(mBMapMan, mMapListener);
        myLocationOverlay = new MyLocationOverlay(mMapView);
        locData = new LocationData();
        myLocationOverlay.setData(locData);
        mMapView.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableCompass();
        mMapView.refresh();
        
        locView = new LocationData();
        for(LocationData locView : mLocations) {
        	locView.accuracy = locData.accuracy;
        }
        
        testUpdateButton = (Button)findViewById(R.id.button1);
        OnClickListener clickListener = new OnClickListener(){
                public void onClick(View v) {
                    testUpdateClick();
                }
            };
        testUpdateButton.setOnClickListener(clickListener);
        /*
        new Thread(new Runnable() {
        	public void run() {

                //while (true) {
                	try {
                		Toast.makeText(Main.this, "LocationsViewThread",
        						Toast.LENGTH_SHORT).show();
        				Thread.sleep(5000);
        			} catch (InterruptedException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
                    String urlPost = "http://121.199.47.152/wt_server/getLocations.php";
                    
                    ArrayList<NameValuePair> postParameter = new ArrayList<NameValuePair>();
                    
                    String returnResponse = null;
        			try {

        				returnResponse = ConnectionHttpClient.executeHttpPost(
        						urlPost, postParameter);
        				String response = returnResponse.toString();
        				
        				Log.i("LOG",""+response);
        				Toast.makeText(Main.this, "response:"+response,
        						Toast.LENGTH_SHORT).show();
        				
        				JSONArray array;
        				array = new JSONArray(response);
        				
        				for (int i = 0; i < array.length(); i++) {
        					JSONObject object = new JSONObject(array.getString(i));
        					locView.latitude = object.getDouble("latitude");
        					locView.longitude = object.getDouble("longitude");
        					mLocations.add(locView);
        				}
        				
        				for(LocationData locView : mLocations) {
        					myLocationOverlay.setData(locView);
        				}
        				mMapView.refresh();
        				mMapController.animateTo(new GeoPoint((int)(locView.latitude* 1e6), (int)(locView.longitude *  1e6)));
        				
        			} catch (Exception error) {
        				//showMessage("Error", "Could not update" + error);
        			}
                //}
            }
          }).start();
          */
        /*
        while (true) {
        	Toast.makeText(Main.this, "LocationsView",
					Toast.LENGTH_SHORT).show();
        	
        	try {
        		Toast.makeText(Main.this, "LocationsView",
						Toast.LENGTH_SHORT).show();
				//Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
            String urlPost = "http://121.199.47.152/wt_server/getLocations.php";
            
            ArrayList<NameValuePair> postParameter = new ArrayList<NameValuePair>();
            
            String returnResponse = null;
			try {

				returnResponse = ConnectionHttpClient.executeHttpPost(
						urlPost, postParameter);
				String response = returnResponse.toString();
				
				Log.i("LOG",""+response);
				Toast.makeText(Main.this, "response:"+response,
						Toast.LENGTH_SHORT).show();
				
				JSONArray array;
				array = new JSONArray(response);
				
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = new JSONObject(array.getString(i));
					locView.latitude = object.getDouble("latitude");
					locView.longitude = object.getDouble("longitude");
					mLocations.add(locView);
				}
				
				for(LocationData locView : mLocations) {
					myLocationOverlay.setData(locView);
				}
				mMapView.refresh();
				mMapController.animateTo(new GeoPoint((int)(locView.latitude* 1e6), (int)(locView.longitude *  1e6)));
				
			} catch (Exception error) {
				//showMessage("Error", "Could not update" + error);
			}
        }
        */
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }
    
    
    @Override
    protected void onDestroy() {
        if (mLocClient != null)
            mLocClient.stop();
        mMapView.destroy();
        if (mBMapMan != null) {
        	mBMapMan.destroy();
        	mBMapMan = null;
        }
        super.onDestroy();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
        
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMapView.onRestoreInstanceState(savedInstanceState);
    }
    
    public void testUpdateClick(){
        mLocClient.requestLocation();
    }

	
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return ;
            
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            locData.accuracy = location.getRadius();
            locData.direction = location.getDerect();
            myLocationOverlay.setData(locData);
            mMapView.refresh();
            mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
            
            String urlPost = "http://121.199.47.152/wt_server/updateLocation.php";
            
            ArrayList<NameValuePair> postParameter = new ArrayList<NameValuePair>();
            
            postParameter.add(new BasicNameValuePair("id_user",
					Integer.toString(mUserID)));
            postParameter.add(new BasicNameValuePair("latitude",
					Double.toString(locData.latitude)));
            postParameter.add(new BasicNameValuePair("longitude",
					Double.toString(locData.longitude)));
            
            String returnResponse = null;
			try {
				Log.i("LOG",""+postParameter);
				Toast.makeText(Main.this, "postParameter:"+postParameter,
						Toast.LENGTH_SHORT).show();
				returnResponse = ConnectionHttpClient.executeHttpPost(
						urlPost, postParameter);
				String response = returnResponse.toString();
				
				Log.i("LOG",""+response);
				Toast.makeText(Main.this, "response:"+response,
						Toast.LENGTH_SHORT).show();

				if (response.charAt(0) == '1') {
					//showMessage("Registered", "User registered");
				} else {
					//showMessage("Error", "Update Failed");
				}
			} catch (Exception error) {
				//showMessage("Error", "Could not update" + error);
			}
			

        	Toast.makeText(Main.this, "LocationsView",
					Toast.LENGTH_SHORT).show();
        	/*
        	try {
        		Toast.makeText(Main.this, "LocationsView",
						Toast.LENGTH_SHORT).show();
				//Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} */
            String urlPost2 = "http://121.199.47.152/wt_server/getLocations.php";
            
            ArrayList<NameValuePair> postParameter2 = new ArrayList<NameValuePair>();
            
            String returnResponse2 = null;
			try {

				returnResponse2 = ConnectionHttpClient.executeHttpPost(
						urlPost2, postParameter2);
				String response = returnResponse2.toString();
				
				Log.i("LOG",""+response);
				Toast.makeText(Main.this, "response:"+response,
						Toast.LENGTH_SHORT).show();
				
				JSONArray array;
				array = new JSONArray(response);
				
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = new JSONObject(array.getString(i));
					locView.latitude = object.getDouble("latitude");
					locView.longitude = object.getDouble("longitude");
					mLocations.add(locView);
				}
				
				for(LocationData locView : mLocations) {
					myLocationOverlay.setData(locView);
				}
				mMapView.refresh();
				mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
				
			} catch (Exception error) {
				//showMessage("Error", "Could not update" + error);
			}
        }
        
        public void showMessage(String Title, String Text) {
    		AlertDialog.Builder message = new AlertDialog.Builder(Main.this);
    		message.setTitle(Title);
    		message.setMessage(Text);
    		message.setNeutralButton("Ok", null);
    		message.show();
    	}
        
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null){
                return ;
            }
        }
    }

}
