package com.fow.core.platform.leanCloud.request.installation;

/**
 * Created by user on 2016-08-08.
 */
public class InstallationIOSInsertForm extends BaseInstallationInsertForm {
    private String deviceToken;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
