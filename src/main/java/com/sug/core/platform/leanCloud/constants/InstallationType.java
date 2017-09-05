package com.sug.core.platform.leanCloud.constants;

/**
 * Created by user on 2016-08-08.
 */
public enum InstallationType {
    ALL("code"),ANDROID("android"),IOS("ios");

    InstallationType(String code){
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }
}
