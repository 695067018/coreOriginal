package com.sug.core.platform.wx.request;

/**
 * Created by A on 2015/8/27.
 */
public class SendAllMpNews {

    public SendAllMpNews(String media_id) {
        this.media_id = media_id;
    }

    private String media_id;

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }
}
