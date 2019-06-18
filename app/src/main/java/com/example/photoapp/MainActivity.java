package com.example.photoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_PICK_IMAGEFILE = 1000;

    List<Post> posts = new ArrayList<Post>();
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
    final RecyclerView.Adapter mAdapter = new timelineViewAdapter(this, posts);
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.postButton);

        recyclerView = findViewById(R.id.timeline);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        //投稿ボタンが押されたら
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_PICK_IMAGEFILE);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        //ギャラリーから戻ってきたら
        if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();

                try {
                    Bitmap bmp = getBitmapFromUri(uri);

                    //bitmap→byte配列(jpg)に変換
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageData = baos.toByteArray();

                    // タスクの生成
                    Client client = new Client(imageData);
                    client.setOnCallBack(new Client.CallBackTask(){
                        @Override
                        public void CallBack(List<byte[]> result) {
                            super.CallBack(result);
                            //処理
                            for (int i=0; i<result.size(); i++){
                                Post post = new Post(result.get(i));
                                posts.add(0, post);
                                mAdapter.notifyItemInserted(0);
                                recyclerView.smoothScrollToPosition(0);
                                Log.d("CallBack", "add new post");
                            }
                        }
                    });

                    client.execute();

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("onActivityResult", "e");
                }
            }
        }
    }

    //画像のbitmap取得
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
