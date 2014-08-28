package com.neolee.androidsocketclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.iflytek.speech.ErrorCode;
import com.iflytek.speech.ISpeechModule;
import com.iflytek.speech.InitListener;
import com.iflytek.speech.SpeechConstant;
import com.iflytek.speech.SpeechSynthesizer;
import com.iflytek.speech.SynthesizerListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements Runnable {
	private static String TAG = "TtsDemo"; 	
    private TextView tv_msg = null;
    private EditText ed_msg = null;
    private Button btn_send = null;

    private static final String HOST = "192.168.1.101";
    private static final int PORT = 9999;
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String content = "您好！";
    
    private SpeechSynthesizer mTts;
    private Toast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
		
        tv_msg = (TextView) findViewById(R.id.TextView);
        ed_msg = (EditText) findViewById(R.id.EditText01);
        btn_send = (Button) findViewById(R.id.Button02);
        
        mTts = new SpeechSynthesizer(this, mTtsInitListener);
        mToast = Toast.makeText(this,"",Toast.LENGTH_LONG);
        
        mHandler.sendMessage(mHandler.obtainMessage());

        try {
            socket = new Socket(HOST, PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())), true);
        } catch (IOException ex) {
            ex.printStackTrace();
            ShowDialog("login exception" + ex.getMessage());
        }
        btn_send.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String msg = ed_msg.getText().toString();
                if (socket.isConnected()) {
                    if (!socket.isOutputShutdown()) {
                        out.println(msg);
                    }
                }
            }
        });
        new Thread(MainActivity.this).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


    public void ShowDialog(String msg) {
        new AlertDialog.Builder(this).setTitle("notification").setMessage(msg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                }).show();
    }

    public void run() {
        try {
            while (true) {
                if (socket.isConnected()) {
                    if (!socket.isInputShutdown()) {
                        if ((content = in.readLine()) != null) {
                            //content += "\n";
                            mHandler.sendMessage(mHandler.obtainMessage());
                        } else {

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //showTip("content : " + content);
            //tv_msg.setText(tv_msg.getText().toString() + content);
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, "local");
			mTts.setParameter(SpeechSynthesizer.VOICE_NAME, "xiaoyan");
			mTts.setParameter(SpeechSynthesizer.SPEED, "50");
			mTts.setParameter(SpeechSynthesizer.PITCH, "50");
			int code = mTts.startSpeaking(content, mTtsListener);
			if(code != 0)
			{
				showTip("start speak error : " + code);
			}else
				showTip("start speak success.");
        }
    };
    

    /**
     * 初期化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {

		@Override
		public void onInit(ISpeechModule arg0, int code) {
			Log.d(TAG, "InitListener init() code = " + code);
        	if (code == ErrorCode.SUCCESS) {
        		//((Button)findViewById(R.id.tts_play)).setEnabled(true);
        	}
		}
    };
        
    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener.Stub() {
        @Override
        public void onBufferProgress(int progress) throws RemoteException {
        	 Log.d(TAG, "onBufferProgress :" + progress);
        	 showTip("onBufferProgress :" + progress);
        }

        @Override
        public void onCompleted(int code) throws RemoteException {
            Log.d(TAG, "onCompleted code =" + code);
            showTip("onCompleted code =" + code);
        }

        @Override
        public void onSpeakBegin() throws RemoteException {
            Log.d(TAG, "onSpeakBegin");
            showTip("onSpeakBegin");
        }

        @Override
        public void onSpeakPaused() throws RemoteException {
        	 Log.d(TAG, "onSpeakPaused.");
        	 showTip("onSpeakPaused.");
        }

        @Override
        public void onSpeakProgress(int progress) throws RemoteException {
        	Log.d(TAG, "onSpeakProgress :" + progress);
        	showTip("onSpeakProgress :" + progress);
        }

        @Override
        public void onSpeakResumed() throws RemoteException {
        	Log.d(TAG, "onSpeakResumed.");
        	showTip("onSpeakResumed");
        }
    };
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTts.stopSpeaking(mTtsListener);
        // 退出时释放连接
        mTts.destory();
    }
	
	private void showTip(final String str)
	{
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mToast.setText(str);
				mToast.show();
		    }
		});
	}
}
