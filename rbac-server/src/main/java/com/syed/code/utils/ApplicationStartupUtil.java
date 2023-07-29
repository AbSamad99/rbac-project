package com.syed.code.utils;

import com.syed.code.services.jwt.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupUtil implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private EntityClassMapUtil entityClassMapUtil;

    @Autowired
    private EntityServiceMapUtils entityServiceMapUtils;

    @Autowired
    private EntityAuditDetailsServiceMapUtil entityAuditDetailsServiceMapUtil;

    @Autowired
    private EntityGridSanitizationMapUtil entityGridSanitizationMapUtil;

    @Autowired
    private JwtServiceImpl jwtServiceImpl;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        entityClassMapUtil.init();
        entityServiceMapUtils.init();
        entityAuditDetailsServiceMapUtil.init();
        entityGridSanitizationMapUtil.init();
        jwtServiceImpl.init();
    }
}
