package com.sug.core.platform.wx.dao.entity;

import com.sug.core.platform.wx.dao.entity.base.WxBaseMsgEntity;

/**
 * Created by suggestion on 2015/6/17.
 */
public class WxMsgEventEntity extends WxBaseMsgEntity {
    private String event;

    private String eventKey;

    private String ticket;

    private Double latitude;

    private Double longitude;

    private Double precision;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getPrecision() {
        return precision;
    }

    public void setPrecision(Double precision) {
        this.precision = precision;
    }
}
