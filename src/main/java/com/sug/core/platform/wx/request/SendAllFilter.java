package com.sug.core.platform.wx.request;

/**
 * Created by A on 2015/8/27.
 */
public class SendAllFilter {

    public SendAllFilter(Boolean is_to_all, String group_id) {
        this.is_to_all = is_to_all;
        this.group_id = group_id;
    }

    private Boolean is_to_all;
    private String group_id;

    public Boolean getIs_to_all() {
        return is_to_all;
    }

    public void setIs_to_all(Boolean is_to_all) {
        this.is_to_all = is_to_all;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }
}
