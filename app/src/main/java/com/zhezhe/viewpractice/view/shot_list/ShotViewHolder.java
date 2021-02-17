package com.zhezhe.viewpractice.view.shot_list;

import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zhezhe.viewpractice.R;
import com.zhezhe.viewpractice.view.base.BaseViewHolder;


public class ShotViewHolder extends BaseViewHolder {
    private View cover;
    TextView likeCount;
    TextView bucketCount;
     TextView viewCount;
    SimpleDraweeView simpleDraweeView;

    public ShotViewHolder(View itemVIew) {
        super(itemVIew);
        this.cover = itemVIew.findViewById(R.id.shot_clickable_cover);
        this.likeCount = itemVIew.findViewById(R.id.shot_like_count);
        this.bucketCount = itemVIew.findViewById(R.id.shot_bucket_count);
        this.viewCount = itemVIew.findViewById(R.id.shot_view_count);
        this.simpleDraweeView = itemVIew.findViewById(R.id.shot_image);
    }
}
