package com.zhezhe.viewpractice.view.shot_list;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.zhezhe.viewpractice.R;
import com.zhezhe.viewpractice.objects.Shot;

import java.util.List;

public class ShotListAdapter extends RecyclerView.Adapter {
    // 1 shot
    // 2 loading

    private List<Shot> data;
    private LoadMoreListener listener;
    private boolean showLoading;


    public ShotListAdapter(List<Shot> data, LoadMoreListener listener, boolean showLoading) {
        this.data = data;
        this.listener = listener;
        this.showLoading = showLoading;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 1) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_shot, viewGroup, false);
            return new ShotViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_loading, viewGroup, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final int viewType = getItemViewType(i);
        if (viewType == 2) {
            listener.onLoadMore();
        } else {
            final Shot shot = data.get(i);

            ShotViewHolder shotViewHolder = (ShotViewHolder) viewHolder;
            shotViewHolder.likeCount.setText(shot.likes_count);
            shotViewHolder.bucketCount.setText(shot.likes_count);
            shotViewHolder.viewCount.setText(shot.likes_count);
            shotViewHolder.simpleDraweeView.setController(Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(shot.getImageUrl()))
                    .setAutoPlayAnimations(true)
                    .build());

        }
    }

    @Override
    public int getItemCount() {
        return showLoading ? data.size() + 1 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position < data.size() ? 1 : 2;
    }

    public void append(List<Shot> newLoaded) {
        data.addAll(newLoaded);
        notifyDataSetChanged();
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    interface LoadMoreListener {
        void onLoadMore();
    }
}
