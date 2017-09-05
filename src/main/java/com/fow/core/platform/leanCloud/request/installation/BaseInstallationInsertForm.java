package com.fow.core.platform.leanCloud.request.installation;

import java.util.List;

/**
 * Created by user on 2016-08-08.
 */
public class BaseInstallationInsertForm {
    private String deviceType;
    private List<String> channels;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }
}
