package com.max.videoplayer;

import android.app.Activity;
//import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
public class MainActivity extends Activity {
   private VideoView videoView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        videoView = (VideoView) this.findViewById(R.id.videoView);
        MediaController mc = new MediaController(this);
        videoView.setMediaController(mc);
        //videoView.setVideoURI(Uri.parse(""));
        videoView.setVideoPath("/sdcard/test.mp4");
        videoView.requestFocus();
        videoView.start();
    }
}