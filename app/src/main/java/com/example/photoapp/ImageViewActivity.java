package com.example.photoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class ImageViewActivity extends Activity {
    private float scale = 1f;
    private ScaleGestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_view);

        Intent intent = getIntent();
        Integer data = intent.getIntExtra("ImageData", 0);

        final ImageView imageView = findViewById(R.id.image_view);
        imageView.setImageResource(data);

        class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scale *= detector.getScaleFactor();
                imageView.setScaleX(scale);
                imageView.setScaleY(scale);
                return true;
            }

        }

        detector = new ScaleGestureDetector(this,new ScaleListener());


        Button button = findViewById(R.id.exitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EXITボタンをタップするともとの画面に戻る
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    public boolean onTouchEvent(MotionEvent event) {
        //re-route the Touch Events to the ScaleListener class
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }



}