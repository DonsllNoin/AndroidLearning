package com.example.androidlearning.NotificationTest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.BaseBundle;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidlearning.R;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button sendNotice = findViewById(R.id.send_notice);

        // 绑定监听器
        sendNotice.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.send_notice:

                // 添加页面跳转
                Intent[] intents = new Intent[]{new Intent(this, NotificationActivity.class)};
                PendingIntent pi = PendingIntent.getActivities(this, 0, intents, 0);


                NotificationManager manager = (NotificationManager) getSystemService
                        (NOTIFICATION_SERVICE);

                String id = "test";

                String desc = "123";

                int importance = NotificationManager.IMPORTANCE_HIGH;

                NotificationChannel mChannel = new NotificationChannel(id, "123", importance);
                manager.createNotificationChannel(mChannel);

                Notification notification = new Notification.Builder(this, id)
                        .setContentTitle("This is content title")
                        .setContentText("这里设置不了长文字就离谱")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        // 添加 点击后就跳转
                        .setContentIntent(pi)
                        // 自动取消
                        .setAutoCancel(true)
                        // 设置声音
//                        .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))
                        // 设置震动 ( 静止时长，震动时长，静止时长，震动时长 )（还要先声明权限）
                        .setVibrate(new long[]{0, 1000, 1000, 1000})
                        // 设置 LED 灯闪烁
                        .setLights(Color.GREEN, 1000, 1000)
                        // 全员默认
//                        .setDefaults(Notification.DEFAULT_ALL)
                        // 设置 长通知
                        .setStyle(new Notification.BigTextStyle().bigText("1111111111111111111111111" +
                                "1111111111111111111111111111111111111111111111111111111111111111111" +
                                "1111111111111111111111111111111111111111111111111111111111111111111" +
                                "1111111111111111111111111111111111111111111111111111111111111111111" +
                                "1111111111111111111111111111111111111111111111111111111111111111111" +
                                "1111111111111111111111111111111111111111111111111111111111111111111"))



                        .build();

                manager.notify(1, notification);
                break;

            default:
                break;

        }

    }
}