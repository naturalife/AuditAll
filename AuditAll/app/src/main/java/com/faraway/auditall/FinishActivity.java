package com.faraway.auditall;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.faraway.auditall.Utils.ExcelUtil;

import java.io.File;

/**
 * @author ${Faraway}
 * @description审核完成界面
 * @date ${data}
 **/

public class FinishActivity extends AppCompatActivity  {
    private TextView mTextView;//谢谢
    private ImageView imageViewQuit;//App Logo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        imageViewQuit = findViewById(R.id.imageView_finishActivity_quit);
        mTextView = findViewById(R.id.finish_tv1);

        //App Logo，单击退出
        imageViewQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinishActivity.this,InitialPageActivity.class);
                intent.putExtra("TAG_EXIT", true);
                startActivity(intent);
            }
        });
    }

}
