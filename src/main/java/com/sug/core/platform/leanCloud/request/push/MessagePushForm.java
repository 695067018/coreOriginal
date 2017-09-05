package com.sug.core.platform.leanCloud.request.push;

import java.util.Date;
import java.util.List;

/**
 * Created by user on 2016-08-08.
 */
public class MessagePushForm{
    private List<String> channels;
    private PushData data;
    private Integer expiration_interval;
    private Date expiration_time;
    private PushConditions where;
    private String prod;

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public PushData getData() {
        return data;
    }

    public void setData(PushData data) {
        this.data = data;
    }

    public Integer getExpiration_interval() {
        return expiration_interval;
    }

    public void setExpiration_interval(Integer expiration_interval) {
        this.expiration_interval = expiration_interval;
    }

    public Date getExpiration_time() {
        return expiration_time;
    }

    public void setExpiration_time(Date expiration_time) {
        this.expiration_time = expiration_time;
    }

    public PushConditions getWhere() {
        return where;
    }

    public void setWhere(PushConditions where) {
        this.where = where;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }
}
