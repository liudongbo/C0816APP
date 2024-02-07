package com.xt.mijkplayer.ui.video;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xt.mijkplayer.R;

import java.util.List;

public class VideoListAdapter extends BaseQuickAdapter<VideoPlayerBean, BaseViewHolder> {

    public VideoListAdapter(@Nullable List<VideoPlayerBean> data) {
        super(R.layout.item_video_player, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, VideoPlayerBean item) {
//        holder.setText(R.id.tv_name, item.getName());
        holder.setText(R.id.tv_path, item.getPath());
    }
}
