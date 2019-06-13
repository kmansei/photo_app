package com.example.photoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class timelineViewAdapter extends RecyclerView.Adapter<timelineViewAdapter.ViewHolder> {

    private List<Post> posts;
    private Context context;
    private Activity activity;
    private static final int RESULTCODE = 1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.image_view);
        }
    }

    public timelineViewAdapter(Activity activity, List<Post> postLists) {
        this.posts= postLists;
        this.activity = activity;
        this.context = activity;
    }

    @Override
    public timelineViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Bitmap bmp = posts.get(position).bmp;
        holder.imageView.setImageBitmap(bmp);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageViewActivity.class);

                //bitmap→byte配列(jpg)に変換
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] jpgarr = baos.toByteArray();

                intent.putExtra("ImageData", jpgarr);
                activity.startActivityForResult(intent, RESULTCODE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
