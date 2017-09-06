package com.sug.core.platform.dingtalk.request;

import java.util.List;

public class DingtalkCreateChatForm {
    private String name;
    private String owner;
    private List<String> useridlist;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getUseridlist() {
        return useridlist;
    }

    public void setUseridlist(List<String> useridlist) {
        this.useridlist = useridlist;
    }
}
