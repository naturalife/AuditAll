package com.faraway.auditall.SelectPhoto;


/**
 * @author Fan
 * @description 显示大图，并可滑动
 * @date 2019-08-19
 * @time 13:39
 **/

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.faraway.auditall.AuditPagerActivity;
import com.faraway.auditall.R;
import com.faraway.auditall.SingleFragmentActivity;

import java.util.ArrayList;

public class ShowBigPictureActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private ImageView imageView;//显示放大图片的View
    private ImageView backButton;//返回按钮
    private TextView finishButton;//完成按钮，作用和返回按钮相同

    private int number;//接收的图片数组的编号；
    private int newNumber;//滑动切换的图片数组的编号；
    private int whichActivity;
    final int distance = 50;//手指滑动的最小距离，

    private ArrayList<String> photoPathsList;//接收图片的数组；

    private GestureDetector gestureDetector;//手势感应

    private String selectActivity = "SelectActivity";
    private String finalSelectActivity = "FinalSelectActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_picture);

        imageView = findViewById(R.id.iv_1);
        backButton = findViewById(R.id.arrowBack);
        finishButton = findViewById(R.id.selectStatus);
//        flipper = findViewById(R.id.flipper);
        gestureDetector = new GestureDetector(ShowBigPictureActivity.this, this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            whichActivity = bundle.getInt("whichActivity");
            number = bundle.getInt("number");

            photoPathsList = bundle.getStringArrayList("allphotos");
            Uri uri1 = Uri.parse(photoPathsList.get(number));
            imageView.setImageURI(uri1);
        }

        newNumber = number;

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (whichActivity != 0) {

                    switch (whichActivity) {
                        case 1000:
                            Intent intent11 = new Intent();
                            intent11.setClass(ShowBigPictureActivity.this, SelectActivity.class);
                            setResult(Activity.RESULT_OK,intent11);
                            finish();
                            break;
                        case 2000:
                            Intent intent2 = new Intent();
                            intent2.setClass(ShowBigPictureActivity.this, AuditPagerActivity.class);
                            setResult(Activity.RESULT_OK,intent2);
                            finish();
                            break;
                        default:
                            break;
                    }
                }
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (whichActivity != 0) {

                    switch (whichActivity) {
                        case 1000:
                            Intent intent11 = new Intent();
                            intent11.setClass(ShowBigPictureActivity.this, SelectActivity.class);
                            setResult(Activity.RESULT_OK,intent11);
                            finish();
                            break;
                        case 2000:
                            Intent intent2 = new Intent();
                            intent2.setClass(ShowBigPictureActivity.this, AuditPagerActivity.class);
                            setResult(Activity.RESULT_OK,intent2);
                            finish();
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float X, float Y) {
        if (e1.getX() - e2.getX() > distance) {
            if (newNumber < (photoPathsList.size() - 1)) {
                newNumber++;
                imageView.setImageURI(Uri.parse(photoPathsList.get(newNumber)));
                return true;
            }

        } else {
            if (e2.getX() - e1.getX() > distance) {
                if (newNumber > 0) {
                    newNumber--;
                    imageView.setImageURI(Uri.parse(photoPathsList.get(newNumber)));
                    return true;
                }

            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }


}
