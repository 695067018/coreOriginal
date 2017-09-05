package com.fow.core.platform.wx.dao.entity.base;

/**
 * Created by suggestion on 2015/6/16.
 */
public class WxBaseRespEntity extends WxBaseEntity{

    private Integer funcFlag;

    private String toUserName;

    private String fromUserName;

    private Long createTime;

    private String msgType;

    public Integer getFuncFlag() {
        return funcFlag;
    }

    public void setFuncFlag(Integer funcFlag) {
        this.funcFlag = funcFlag;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
