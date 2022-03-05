package com.example.androidlearning.CameraAlbumTest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androidlearning.MainActivity;
import com.example.androidlearning.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CameraAlbumActivity extends AppCompatActivity {

    private static final String TAG = "CameraAlbumActivity";

    public static final int TAKE_PHOTO = 1;

    public static final int CHOOSE_PHOTO = 2;

    private ImageView picture;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_album);

        // 绑定按钮
        Button takePhoto = findViewById(R.id.take_photo);
        Button chooseFromAlbum = findViewById(R.id.choose_from_album);

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
                    imageUri = FileProvider.getUriForFile(CameraAlbumActivity.this,
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

        // 从相册中选择照片
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 如果该权限用户还没有授权的话，就先让用户授权
                if (ContextCompat.checkSelfPermission(
                        CameraAlbumActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(CameraAlbumActivity.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE

                    }, 1);

                }else {
                    openAlbum();
                }
            }
        });

    }

    // 打开相册
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);

    }

    // 判断用户是否给了权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // resultCode 是在 目标Activity中设置
        switch (requestCode){
            case TAKE_PHOTO:
                Log.i(TAG, "Donsll: requestCode = " + requestCode);
                Log.i(TAG, "Donsll: resultCode = " + resultCode);
                Log.i(TAG, "Donsll: data = " + data);

                // 如果用户拍照了，那就将结果返回到 IV 中展示
                if (resultCode == RESULT_OK){

                    try {
                        // 将 图片 解析成 Bitmap 对象
                        Bitmap bitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);

                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }

                }
                break;

            case CHOOSE_PHOTO :
                if (resultCode == RESULT_OK){

                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19){

                        // 4.4 及以上的系统使用这个方法处理图片
                        // 因为 4.4 之后，选取相册中照片后返回的是一个封装过的 uri，
                        // 必须需要先解析，才能获取到真实的 uri
                        handleImageOnKitKat(data);

                    }else {
                        // 4.4 以下的系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);

                    }

                }


            default:
                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    // 解析 返回的 uri
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();

        // 如果是 document 类型的 uri ，则通过 document id 处理
        if (DocumentsContract.isDocumentUri(this, uri)){
            String docId = DocumentsContract.getDocumentId(uri);

            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1]; // 解析出数字格式的 Id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse(
                        "content://downloads/public_downloads"), Long.valueOf(docId));

                imagePath = getImagePath(contentUri, null);

            }

        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            // 如果是 content 类型的 uri ，则使用普通方式来处理
            imagePath = getImagePath(uri, null);

        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            // 如果是 file 类型的 uri ，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);

    }

    private void handleImageBeforeKitKat(Intent data) {

        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);

    }

    // 获取照片
    private String getImagePath(Uri uri, String selection) {
        String path = null;

        // 通过 uri 和 selection 来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;

    }

    // 展示照片
    private void displayImage(String imagePath) {
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }

    }



}