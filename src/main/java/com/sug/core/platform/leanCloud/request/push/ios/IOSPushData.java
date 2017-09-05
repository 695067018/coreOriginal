package com.sug.core.platform.leanCloud.request.push.ios;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sug.core.platform.leanCloud.request.push.PushData;

/**
 * Created by user on 2016-08-08.
 */
public class IOSPushData extends PushData {
    private String category;
    private IOSAlert alert;
    private Integer badge;
    private String sound;
    @JsonProperty(value = "content-available")
    private Integer contentAvailable;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public IOSAlert getAlert() {
        return alert;
    }

    public void setAlert(IOSAlert alert) {
        this.alert = alert;
    }

    public Integer getBadge() {
        return badge;
    }

    public void setBadge(Integer badge) {
        this.badge = badge;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public Integer getContentAvailable() {
        return contentAvailable;
    }

    public void setContentAvailable(Integer contentAvailable) {
        this.contentAvailable = contentAvailable;
    }
}
