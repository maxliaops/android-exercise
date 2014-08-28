package com.example.camerapreview;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.SurfaceHolder;
import android.view.ViewGroup;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

    @Override
    protected void onPause() {
    	
    }

}


class Preview extends ViewGroup implements SurfaceHolder.Callback {
    Preview(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    	
    }

    public void surfaceCreated(SurfaceHolder holder) {
    	
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    	
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    	
    }
}







