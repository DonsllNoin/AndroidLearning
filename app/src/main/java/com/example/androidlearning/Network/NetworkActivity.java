package com.example.androidlearning.Network;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidlearning.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        Button sendRequest = findViewById(R.id.send_request);
        responseText = findViewById(R.id.response_text);

        sendRequest.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.send_notice:
                sendRequestWithOkHttp();
                break;

            default:
                break;

        }
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    // 创建一个 请求客户端
                    OkHttpClient client = new OkHttpClient();

                    // 创建一个 请求
                    Request request = new Request.Builder()
                            .url("http://www.baidu.com")
                            .build();

                    // 创建一个 响应（ 由客户端发送请求后，服务器返回的响应 ）
                    Response response = client.newCall(request).execute();

                    // 获取 响应
                    String responseData = response.body().string();
                    showResponse(responseData);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }


    private void showResponse(final String response) {

        // 由于安卓是 不允许在子线程中 进行 UI 操作的
        // 因此需要调用这个方法来回到主线程中
        // 子线程 ：线程里的线程
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行 UI 操作，将结果显示在界面上
                responseText.setText(response);
            }
        });

    }
}