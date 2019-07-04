package com.example.photoapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Account");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        //戻るボタンを表示
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //アイコンの色を白くする
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        final EditText ip = findViewById(R.id.ipEdit);
        ip.setText(Client.ip);

        if (Data.user_name != null){
            EditText user_name = findViewById(R.id.userEdit);
            user_name.setText(Data.user_name);
        }

        //ボタンを押したとき
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText user_name = findViewById(R.id.userEdit);
                Data.user_name = user_name.getText().toString();
                Client.ip = ip.getText().toString();
            }
        });
    }

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
