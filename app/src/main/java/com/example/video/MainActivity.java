package com.example.video;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView videoView;

    public static final String TAG = "videoTest";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        checkMyPermissions();


    }

    private void initUI() {
        videoView = (VideoView) findViewById(R.id.video_view);
        Button play = findViewById(R.id.play);
        Button pause = findViewById(R.id.pause);
        Button replay = findViewById(R.id.replay);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        replay.setOnClickListener(this);


    }

    private void checkMyPermissions() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        } else {
            initVideoPath();
        }

    }

    private void initVideoPath() {

        //testLocalPlay
        testLocalPlay();


        // playURL
        testPlayURL();

    }

    private void testLocalPlay() {
        File file = new File(Environment.getExternalStorageDirectory(),"Movies/movie.mp4");
        Log.e(TAG,"path = " + file.getPath());
        if (file.exists()){
            videoView.setVideoPath(file.getPath());
        }
        videoView.start();

    }

    private void testPlayURL() {
        //网络视频
        String videoUrl2 = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4" ;

        Uri uri = Uri.parse( videoUrl2 );

        //设置视频控制器
        videoView.setMediaController(new MediaController(this));

        //播放完成回调
        videoView.setOnCompletionListener( new MyPlayerOnCompletionListener());

        //设置视频路径
        videoView.setVideoURI(uri);

        //开始播放视频
        videoView.start();

        videoView.requestFocus();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    initVideoPath();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }

        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.play:
                Log.e(TAG, "start");
                if (!videoView.isPlaying()) {
                    videoView.start();
                } else {
                    Log.e(TAG, "isPlaying");

                }
                break;


            case R.id.pause:
                Log.e(TAG, "pause");
                if (videoView.isPlaying()) {
                    videoView.pause();
                }
                break;
            case R.id.replay:
                Log.e(TAG, "replay");
                if (videoView.isPlaying()) {
                    videoView.resume();
                }
                break;
            default:
                break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.suspend();
        }
    }


    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText( MainActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }
}