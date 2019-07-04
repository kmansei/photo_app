package com.example.photoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Window;
import android.widget.ImageView;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class ImageViewActivity extends AppCompatActivity {
    private float scale = 1f;
    private ScaleGestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_view);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //戻るボタンを表示
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //アイコンの色を白くする
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        //画像のバイト配列をキャッシュファイルから読み込む
        byte[] jpgImage = null;
        final File f = new File(getCacheDir(), "cache.jpg");
        try (FileInputStream fis = new FileInputStream(f)){
            jpgImage = IOUtils.toByteArray(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
    }

    public boolean onTouchEvent(MotionEvent event) {
        //re-route the Touch Events to the ScaleListener class
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    //ツールバーの各種アイコンが押されたときに呼ばれる(今は戻るボタンだけ)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        boolean result = true;

        switch (id) {
            case android.R.id.home:
                // 矢印ボタンを押すともとの画面に戻る
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                result = super.onOptionsItemSelected(item);
        }

        return result;
    }
}
