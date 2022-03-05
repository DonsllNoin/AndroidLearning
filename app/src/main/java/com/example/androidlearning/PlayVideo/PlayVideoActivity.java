package com.example.androidlearning.PlayVideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.androidlearning.R;

import java.io.File;

public class PlayVideoActivity extends AppCompatActivity implements View.OnClickListener{

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        videoView = findViewById(R.id.video_view);

        Button play = findViewById(R.id.play_video);
        Button pause = findViewById(R.id.pause_video);
        Button replay = findViewById(R.id.replay_video);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        replay.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(PlayVideoActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(PlayVideoActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);

        }else {
            initVideoPath();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initVideoPath();
                }else{
                    Toast.makeText(this, "无权限使用此程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }

    }

    private void initVideoPath() {
        // 指定文件路径
        File file = new File(Environment.getExternalStorageDirectory(), "movie.mp4");
        videoView.setVideoPath(file.getPath());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 开始播放
            case R.id.play_video:
                if (!videoView.isPlaying()){
                    videoView.start();
                }
                break;

                // 暂停播放
            case R.id.pause_video:
                if (videoView.isPlaying()){
                    videoView.pause();
                }
                break;

                // 重新播放
            case R.id.replay_video:
                if (videoView.isPlaying()){
                    videoView.resume();
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null){
            // 释放资源
            videoView.suspend();
        }

    }
}