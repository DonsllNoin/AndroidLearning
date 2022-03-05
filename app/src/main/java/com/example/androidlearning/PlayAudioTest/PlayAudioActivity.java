package com.example.androidlearning.PlayAudioTest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.PatternMatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidlearning.R;

import java.io.File;
import java.io.IOException;

public class PlayAudioActivity extends AppCompatActivity implements View.OnClickListener{

    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);

        Button play = findViewById(R.id.play);
        Button pause = findViewById(R.id.pause);
        Button stop = findViewById(R.id.stop);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(PlayAudioActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(PlayAudioActivity.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);

        }else {
            // 初始化 MediaPlayer
            initMediaPlayer();
        }
    }

    private void initMediaPlayer() {

        try {
            // 指定音频文件的路径
            File file = new File(Environment.getExternalStorageDirectory(),"music.mp3");
            // 让 MediaPlayer 进入准备状态
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode){
            case 1:
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initMediaPlayer();
                }else {
                    Toast.makeText(this, "没有权限使用该程序", Toast.LENGTH_SHORT).show();

                    // 结束 Activity
                    finish();
                }
                break;

            default:

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                if (!mediaPlayer.isPlaying()){
                    // 如果还没播放，就先开始播放
                    mediaPlayer.start();
                }
                break;

            case R.id.pause:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                break;

            case R.id.stop:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                break;

            default:
                break;
        }

    }

    @Override
    // 关闭程序时，关闭音视频的播放
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.stop();
            // 释放资源
            mediaPlayer.release();
        }
    }
}