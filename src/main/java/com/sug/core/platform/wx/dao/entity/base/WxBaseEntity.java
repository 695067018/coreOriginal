package com.sug.core.platform.wx.dao.entity.base;

import java.util.Date;

/**
 * Created by suggestion on 2015/6/16.
 */
public class WxBaseEntity {
    private Long id;

    private Date createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
