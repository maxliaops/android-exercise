package com.neolee.socketclienttest;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

public class SocketClientTestActivity extends Activity {
	//声明文本视图TextView
	private TextView myTextView;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());

        //通过findViewById 方法找到TextView
        myTextView = (TextView)findViewById(R.id.msgTextView01);
        try{
        	//实例化Socket
        	Socket socket = new Socket("192.168.1.100",10000);
        	//获得输入流
        	InputStream in = socket.getInputStream();
        	StringBuilder sb = new StringBuilder();
        	int cr = 0;
        	//
        	while(cr != -1)
        	{
        		cr = in.read();

        		if(cr != -1)
        		{
        			sb.append((char)cr);
        		}
        	}

        	//TextView显示
        	myTextView.setText(sb.toString());
        }catch(UnknownHostException e){
        	e.printStackTrace();
        }catch(IOException e){
        	e.printStackTrace();
        }
    }

}
