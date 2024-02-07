package com.xt.mijkplayer.ui.video;

import java.io.Serializable;

public class VideoPlayerBean implements Serializable {
    public String name;
    public String path;

    public VideoPlayerBean(String n, String p){
        this.name=n;
        this.path=p;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
