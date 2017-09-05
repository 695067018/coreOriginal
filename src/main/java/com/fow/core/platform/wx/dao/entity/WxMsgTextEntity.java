package com.fow.core.platform.wx.dao.entity;

import com.fow.core.platform.wx.dao.entity.base.WxBaseMsgEntity;

/**
 * Created by suggestion on 2015/6/16.
 */
public class WxMsgTextEntity extends WxBaseMsgEntity {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
