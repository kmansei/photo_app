package com.example.photoapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class timelineViewAdapter extends RecyclerView.Adapter<timelineViewAdapter.ViewHolder> {

    private List<Integer> iImages;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.image_view);
        }
    }

    public timelineViewAdapter(List<Integer> itemImages) {
        this.iImages = itemImages;
    }

    @Override
    public timelineViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Integer data;
        data = iImages.get(position);
        holder.imageView.setImageResource(iImages.get(position));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromDataset(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return iImages.size();
    }

    protected void removeFromDataset(Integer data){
        for(int i=0; i<iImages.size(); i++){
            if(iImages.get(i).equals(data)){
                iImages.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }
}
