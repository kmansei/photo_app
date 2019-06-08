package com.example.photoapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.List;

public class timelineViewAdapter extends RecyclerView.Adapter<timelineViewAdapter.ViewHolder> {

    private List<Integer> iImages;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.image_view);
        }
    }

    public timelineViewAdapter(Context context, List<Integer> itemImages) {
        this.iImages = itemImages;
        this.context = context;
    }

    @Override
    public timelineViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Integer data;
        data = iImages.get(position);
        holder.imageView.setImageResource(data);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final int posi = holder.getAdapterPosition();
                //removeFromDataset(data, position);
                showDialog(data, position, holder.imageView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return iImages.size();
    }

    protected void removeFromDataset(Integer data, int position){
        iImages.remove(position);
        notifyItemRemoved(position);

    }

    protected void showDialog(Integer data, int position, ImageView tapView){
        ImageView imageView = new ImageView(context);
        Bitmap bitmap = ((BitmapDrawable)tapView.getDrawable()).getBitmap();
        imageView.setImageBitmap(bitmap);
        // ディスプレイの幅を取得する（API 13以上）
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        float factor =  width / bitmap.getWidth();
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        // ダイアログを作成する
        Dialog dialog = new Dialog(context);
        // タイトルを非表示にする
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(imageView);
        dialog.getWindow().setLayout((int)(bitmap.getWidth()*factor), (int)(bitmap.getHeight()*factor));
        // ダイアログを表示する
        dialog.show();
    }
}
