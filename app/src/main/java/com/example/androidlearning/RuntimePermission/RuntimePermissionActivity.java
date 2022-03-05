package com.example.androidlearning.RuntimePermission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidlearning.R;

public class RuntimePermissionActivity extends AppCompatActivity {

    private Button call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_permission);

        call = findViewById(R.id.make_call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 判断用户是否已经给我们授权了，第一个接收的参数：Context ，第二个参数：权限名
                if (ContextCompat.checkSelfPermission(

                        RuntimePermissionActivity.this, Manifest.permission.CALL_PHONE)

                        // 如果相等的话就说明已经授权，不相等则说明还没有授权
                        != PackageManager.PERMISSION_GRANTED){

                    // 开始向用户获取授权
                    ActivityCompat.requestPermissions(

                            // 第一个参数：Activity 实例，
                            // 第二个参数：一个包含了权限名的 String[] 数组，
                            // 第三个参数：请求码，唯一即可。
                            RuntimePermissionActivity.this,

                            new String[]{ Manifest.permission.CALL_PHONE

                            }, 1);
                }else {

                    // 如果已经授权，则直接调用打电话功能即可
                    call();
                }
            }
        });

    }

    @Override
    // 接收 用户选择的权限，回收的结果 会放到 grantResults 参数当中。
    // 如果用户选择同意的话，那就直接打电话。不同意的话就放弃操作
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    call();

                }else {
                    Toast.makeText(this, "You denied the permission",
                            Toast.LENGTH_SHORT).show();

                }
                break;
            default:
        }

    }

    private void call(){

        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10086"));
            startActivity(intent);
        }catch (SecurityException e){
            e.printStackTrace();
        }

    }

}