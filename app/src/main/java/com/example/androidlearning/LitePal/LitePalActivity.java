package com.example.androidlearning.LitePal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidlearning.R;

import org.litepal.LitePal;

public class LitePalActivity extends AppCompatActivity {

    private Button button1, button2, button3, button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lite_pal);

        button1 = findViewById(R.id.create_database);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 每次创建的时候都要更新一下版本
                LitePal.getDatabase();

            }
        });

    }
}
