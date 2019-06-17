package com.example.photoapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class ImageViewActivity extends Activity {
    private float scale = 1f;
    private ScaleGestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_view);

        //画像のバイト配列をキャッシュファイルから読み込む
        byte[] jpgImage = null;
        final File f = new File(getCacheDir(), "cache.jpg");
        try (FileInputStream fis = new FileInputStream(f)){
            jpgImage = IOUtils.toByteArray(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Post post = (Post)getIntent().getSerializableExtra("Post");
        Bitmap bmp = BitmapFactory.decodeByteArray(jpgImage, 0, jpgImage.length);

        //mbpをセットして表示
        final ImageView imageView = findViewById(R.id.image_view);
        imageView.setImageBitmap(bmp);

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
