package com.sug.core.platform.dingtalk.request;

import java.util.List;

public class DingtalkUpdateChatForm {
    private String chatid;
    private String name;
    private String owner;
    private List<String> add_useridlist;
    private List<String> del_useridlist;

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

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public List<String> getAdd_useridlist() {
        return add_useridlist;
    }

    public void setAdd_useridlist(List<String> add_useridlist) {
        this.add_useridlist = add_useridlist;
    }

    public List<String> getDel_useridlist() {
        return del_useridlist;
    }

    public void setDel_useridlist(List<String> del_useridlist) {
        this.del_useridlist = del_useridlist;
    }
}
