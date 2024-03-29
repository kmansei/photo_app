package com.example.photoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_PICK_IMAGEFILE = 1000;
    private static final int RESULT_ACCOUNT = 500;

    List<Post> posts = new ArrayList<Post>();
    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
    final RecyclerView.Adapter mAdapter = new timelineViewAdapter(this, posts);
    RecyclerView recyclerView;
    MenuItem addButton;
    Intent intent;

    //ボトムナビゲーションの処理
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_post:
                    addButton = item;
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, RESULT_PICK_IMAGEFILE);
                    return true;
                case R.id.navigation_account:
                    intent = new Intent(getApplicationContext(), AccountActivity.class);
                    startActivityForResult(intent, RESULT_ACCOUNT);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle("Photo app");
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        recyclerView = findViewById(R.id.timeline);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        navView.setSelectedItemId(R.id.navigation_home);

        //ギャラリーから戻ってきたら
        if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                addButton.setEnabled(false);
                try {
                    Bitmap bmp = getBitmapFromUri(uri);

                    //bitmap→byte配列(jpg)に変換
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageData = baos.toByteArray();
                    int id = posts.size();
                    // タスクの生成

                    String name = Data.user_name;
                    if (name == null){
                        name = "user";
                    }

                    Client client = new Client(id, imageData, name);
                    client.setOnCallBack(new Client.CallBackTask(){
                        @Override
                        public void CallBack(List<Post> result) {
                            super.CallBack(result);
                            //処理
                            for (int i=0; i<result.size(); i++){
                                Post post = new Post(result.get(i).imageData, result.get(i).user_name);
                                posts.add(0, post);
                                mAdapter.notifyItemInserted(0);
                                recyclerView.smoothScrollToPosition(0);
                            }
                            addButton.setEnabled(true);
                        }
                    });

                    client.execute();

                } catch (IOException e) {
                    e.printStackTrace();
                    addButton.setEnabled(true);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.updateButton:
                //ボタンを押せなくする
                item.setEnabled(false);
                item.setIcon(R.drawable.sync_disabled);

                int id = posts.size();
                // タスクの生成

                Client client = new Client(id, null, "user");
                client.setOnCallBack(new Client.CallBackTask(){
                    @Override
                    public void CallBack(List<Post> result) {
                        super.CallBack(result);
                        //処理
                        for (int i=0; i<result.size(); i++){
                            Post post = new Post(result.get(i).imageData, result.get(i).user_name);
                            posts.add(0, post);
                            mAdapter.notifyItemInserted(0);
                            recyclerView.smoothScrollToPosition(0);
                            Log.d("CallBack", "add new post");
                        }
                        //ボタンを押せるようにする
                        item.setEnabled(true);
                        item.setIcon(R.drawable.sync);
                    }
                });

                client.execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
