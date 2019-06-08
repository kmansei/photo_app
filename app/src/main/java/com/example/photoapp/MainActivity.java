package com.example.photoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //初期に表示するダミー画像
    private static final Integer[] photos = {
            R.drawable.dummy, R.drawable.dummy2
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.timeline);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        //imageをコピー
        final List<Integer> itemImages = new ArrayList<>(Arrays.asList(photos));

        final RecyclerView.Adapter mAdapter = new timelineViewAdapter(itemImages);
        recyclerView.setAdapter(mAdapter);

        Button button = findViewById(R.id.postButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemImages.size() % 2 == 0) {
                    itemImages.add(0, R.drawable.dummy2);
                } else {
                    itemImages.add(0, R.drawable.dummy);
                }

                mAdapter.notifyItemInserted(0);
            }
        });


    }
}
