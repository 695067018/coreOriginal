package com.sug.core.platform.leanCloud.response;

import java.util.Date;

/**
 * Created by user on 2016-08-08.
 */
public class MessagePushResponse{
    private String objectId;
    private Date createdAt;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
