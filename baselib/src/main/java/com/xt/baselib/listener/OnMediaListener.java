package com.xt.baselib.listener;

/**
 * 监听播放进度
 */
public interface OnMediaListener {

    /**
     * 开发播放
     */
    void onStart();

    /**
     * 暂停播放
     */
    void onPause();


    /**
     * 播放进度
     *
     * @param progress // 正在播放到哪儿了
     * @param duration // 视频总长都
     */
    void onProgress(int progress, int duration);

    /**
     * 更改全（单）屏
     */
    void onChangeScreen(boolean isPortrait);

    /**
     * 播放完成
     */
    void onEndPlay();

}
