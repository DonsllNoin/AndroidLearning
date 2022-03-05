package com.example.androidlearning.CameraAlbumTest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.androidlearning.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class UseCameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraAlbumActivity";

    // 请求码（用在接收返回值
    public static final int TAKE_PHOTO = 1;

    private ImageView picture;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_camera);

        // 绑定按钮
        Button takePhoto = findViewById(R.id.take_photo);

        picture = findViewById(R.id.picture);

        // 直接拍照
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 创建 File 对象用于存储拍照后的照片（ 获取的照片存放在 Cache 文件夹下 ）
                // Cache ：应用关联缓存目录，就是专门用来存放应用的缓存文件的
                // 调用 getExternalCacheDir() 可以得到这个目录
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");

                try {
                    if (outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 存储照片路径（创建文件提供器）
                if (Build.VERSION.SDK_INT >= 24){
                    imageUri = FileProvider.getUriForFile(UseCameraActivity.this,
                            "com.example.androidlearning.fileprovider", outputImage);
                }else {
                    imageUri = Uri.fromFile(outputImage);
                }

                // 启动相机
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                // 指定图片的输出地址
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // resultCode 是在 目标Activity中设置
        switch (requestCode) {
            case TAKE_PHOTO:

                // 如果用户拍照了，那就将结果返回到 IV 中展示
                if (resultCode == RESULT_OK) {

                    try {
                        // 将 图片 解析成 Bitmap 对象
                        Bitmap bitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                break;

        }

    }
}