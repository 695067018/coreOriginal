package com.fow.core.platform.leanCloud.request.push;

/**
 * Created by user on 2016-08-08.
 */
public abstract class PushData {

    private BaseCustomParams pushCustomParams;

    public BaseCustomParams getPushCustomParams() {
        return pushCustomParams;
    }

    public void setPushCustomParams(BaseCustomParams pushCustomParams) {
        this.pushCustomParams = pushCustomParams;
    }
}
