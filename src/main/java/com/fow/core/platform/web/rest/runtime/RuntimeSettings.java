package com.fow.core.platform.web.rest.runtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by greg.chen on 14-10-15.
 */
@Component
public class RuntimeSettings {


    @Value("${spring.profiles.active}")
    private RuntimeEnvironment environment;

    public RuntimeEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(RuntimeEnvironment environment) {
        this.environment = environment;
    }
}
