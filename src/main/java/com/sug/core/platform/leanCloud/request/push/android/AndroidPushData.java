package com.sug.core.platform.leanCloud.request.push.android;

import com.sug.core.platform.leanCloud.request.push.PushData;

/**
 * Created by user on 2016-08-08.
 */
public class AndroidPushData extends PushData {
    private String title;
    private String alert;
    private String action;
    private Boolean silent;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean getSilent() {
        return silent;
    }

    public void setSilent(Boolean silent) {
        this.silent = silent;
    }
}
