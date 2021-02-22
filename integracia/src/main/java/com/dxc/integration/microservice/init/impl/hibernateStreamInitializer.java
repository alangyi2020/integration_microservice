package com.dxc.integration.microservice.init.impl;

import com.dxc.integration.microservice.init.StreamInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class hibernateStreamInitializer extends StreamInitializer {
    private static final Logger LOG = LoggerFactory.getLogger(hibernateStreamInitializer.class);
    @Override
    public void init() {
        LOG.info("Hibernate logging...");
    }
}
