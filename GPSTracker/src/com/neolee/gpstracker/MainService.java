package com.neolee.gpstracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

public class MainService extends Service {
	private LocationManager locationManager;
	private Location location;
	private String provider;
	private Address address;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	

    @Override
    public void onCreate() {
        // The service is being created
    	Toast.makeText(this, "start service", Toast.LENGTH_SHORT).show(); 
    	
    	// Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.
        Thread thr = new Thread(null, mTask, "MainService");
        thr.start();
    }

    /**
     * The function that runs in our worker thread
     */
    Runnable mTask = new Runnable() {
        public void run() {
            // Normally we would do some work here...
        	
        	// 获取LocationManager服务
            locationManager =(LocationManager) MainService.this.getSystemService(Context.LOCATION_SERVICE);

            // 获取Location Provider
            getProvider();

            // 如果未设置位置源，打开GPS设置界面
            //openGPS();
            
            // 获取位置
            location = locationManager.getLastKnownLocation(provider);

            // 显示位置信息
            updateWithNewLocation(location);

            // 注册监听器locationListener，第2、3个参数可以控制接收gps消息的频度以节省电力。第2个参数为毫秒，
            // 表示调用listener的周期，第3个参数为米,表示位置移动指定距离后就调用listener
            locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);

        	while(true);
        	
            // Done with our work...  stop the service!
            //MainService.this.stopSelf();
        }
    };
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
    	Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        
    	// We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
    

    @Override
    public void onDestroy() {
      Toast.makeText(this, "stop service", Toast.LENGTH_SHORT).show(); 
    }

    // 获取Location Provider
    private void getProvider(){
    	// 构建位置查询条件
        Criteria criteria = new Criteria();

        // 查询精度：高
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        // 是否查询海拨：否
        //criteria.setAltitudeRequired(false);

        // 是否查询方位角:否
        //criteria.setBearingRequired(false);

        // 是否允许付费：是
        //criteria.setCostAllowed(true);

        // 电量要求：低
        criteria.setPowerRequirement(Criteria.POWER_HIGH);

        // 返回最合适的符合条件的provider，第2个参数为true说明,如果只有一个provider是有效的,则返回当前provider
        provider = locationManager.getBestProvider(criteria,true);   

    }

    // 判断是否开启GPS，若未开启，打开GPS设置界面
    private void openGPS() {        

        if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
        ||locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
        ) {

           Toast.makeText(this, "位置源已设置！", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "位置源未设置！", Toast.LENGTH_SHORT).show();

        // 转至GPS设置界面
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);

        startActivity(intent); 

    }
    
    // Gps消息监听器
    private final LocationListener locationListener = new LocationListener(){

    	// 位置发生改变后调用
        public void onLocationChanged(Location location) {

       updateWithNewLocation(location);

        }

        // provider被用户关闭后调用
        public void onProviderDisabled(String provider){

        updateWithNewLocation(null);

        }

        // provider被用户开启后调用
        public void onProviderEnabled(String provider){ }

        // provider状态变化时调用
        public void onStatusChanged(String provider, int status, Bundle extras){ }

    };


    // Gps监听器调用，处理位置信息
    private void updateWithNewLocation(Location location) {

        String latLongString;

        //TextView myLocationText= (TextView)findViewById(R.id.text);

        if (location != null) {

        	double lat =location.getLatitude();

        	double lng =location.getLongitude();

        	latLongString = "纬度:" + lat + "/n经度:" + lng;

        } else {

        	latLongString = "无法获取地理信息";

        }

        //Toast.makeText(this, latLongString, Toast.LENGTH_LONG).show();
        //myLocationText.setText("您当前的位置是:/n" +

        //latLongString+"/n"+getAddressbyGeoPoint(location));

    }


}
