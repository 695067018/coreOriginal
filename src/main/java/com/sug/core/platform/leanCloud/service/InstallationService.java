package com.sug.core.platform.leanCloud.service;

import com.sug.core.platform.exception.ResourceNotFoundException;
import com.sug.core.util.StringUtils;
import com.sug.core.platform.leanCloud.LeanCloudClient;
import com.sug.core.platform.leanCloud.constants.InstallationType;
import com.sug.core.platform.leanCloud.request.installation.BaseInstallationInsertForm;
import com.sug.core.platform.leanCloud.request.installation.InstallationAndroidInsertForm;
import com.sug.core.platform.leanCloud.request.installation.InstallationIOSInsertForm;
import com.sug.core.platform.leanCloud.response.InstallationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by user on 2016-08-08.
 */
@Service
public class InstallationService {

    private static final String URI= "https://leancloud.cn/1.1/installations";

    @Autowired
    private LeanCloudClient leanCloudClient;

    public InstallationResponse insert(String deviceToken,InstallationType type) throws Exception {
        if(!StringUtils.hasText(deviceToken)){
            throw new ResourceNotFoundException("no deviceToken");
        }

        switch (type){
            case ANDROID:
                return insertAndroidInstallation(deviceToken);
            case IOS:
                return insertIOSInstallation(deviceToken);
        }

        return null;
    }

    private InstallationResponse insertAndroidInstallation(String installationId) throws Exception {
        InstallationAndroidInsertForm form = new InstallationAndroidInsertForm();
        form.setInstallationId(installationId);
        form.setDeviceType(InstallationType.ANDROID.getCode());

        return this.insert(form);
    }

    private InstallationResponse insertIOSInstallation(String deviceToken) throws Exception {
        InstallationIOSInsertForm form = new InstallationIOSInsertForm();
        form.setDeviceToken(deviceToken);
        form.setDeviceToken(InstallationType.IOS.getCode());

        return this.insert(form);
    }

    public InstallationResponse insert(BaseInstallationInsertForm form) throws Exception {
        return leanCloudClient.send(URI,InstallationResponse.class,form);
    }
}
