package com.brick.demo.auth.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAccountManager implements AccountManager {
    protected final Logger logger;

    public AbstractAccountManager() {
        logger = LoggerFactory.getLogger(getClass());
        logger.info("Created" + getImplInfo() + "account-manager");
    }

    @Override
    public String getImplInfo() {
        String myClassName = getClass().getSimpleName();
        int ix = myClassName.indexOf("AccountManager");
        return ix == -1 ? "UNKNOWN" : myClassName.substring(0, ix).toUpperCase(); //AccountManager를 이름에 포함한 구현체 여부.
    }
}
