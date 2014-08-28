package com.example.camerapreview;

import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    private Preview mPreview;
    Camera mCamera;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        mPreview = new Preview(this);
        setContentView(mPreview);
        
        mCamera = Camera.open();
        mPreview.setCamera(mCamera);
	}

    @Override
    protected void onPause() {
    	super.onPause();
    	
    	if (mCamera != null) {
            mPreview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
    }

}


class Preview extends ViewGroup implements SurfaceHolder.Callback {
	private final String TAG = "Preview";
	
	SurfaceView mSurfaceView;
	SurfaceHolder mHolder;
	Camera mCamera;
	
    Preview(Context context) {
        super(context);
        
        mSurfaceView = new SurfaceView(context);
        addView(mSurfaceView);
        
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
    }

    public void setCamera(Camera camera) {
    	mCamera = camera;
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed && getChildCount() > 0) {
            final View child = getChildAt(0);

            final int width = r - l;
            final int height = b - t;

            int previewWidth = width;
            int previewHeight = height;

            // Center the child SurfaceView within the parent.
            if (width * previewHeight > height * previewWidth) {
                final int scaledChildWidth = previewWidth * height / previewHeight;
                child.layout((width - scaledChildWidth) / 2, 0,
                        (width + scaledChildWidth) / 2, height);
            } else {
                final int scaledChildHeight = previewHeight * width / previewWidth;
                child.layout(0, (height - scaledChildHeight) / 2,
                        width, (height + scaledChildHeight) / 2);
            }
        }
    }


    public void surfaceCreated(SurfaceHolder holder) {
    	try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
            }
        } catch (IOException exception) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
        }
    	mCamera.startPreview();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    	if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    	
    }
}







