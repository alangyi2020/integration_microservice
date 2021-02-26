package com.dxc.integration.microservice.runner.impl;

import com.dxc.integration.microservice.runner.StreamRunner;
import com.dxc.soapcall.getKonanieDetail;
import com.dxc.soapcall.vyhladajKonanie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component

public class hibernateStreamRunner extends StreamRunner {
    private static final Logger LOG = LoggerFactory.getLogger(hibernateStreamRunner.class);
    @Override
    public void start() throws Exception {
        vyhladajKonanie vk = new vyhladajKonanie();
        String ret_val = vk.callVyhladajKonanie(1206);
        LOG.info("Retval vyhladajKonanie = " + ret_val);
        
        getKonanieDetail gk = new getKonanieDetail();
        String ret_val_gk = gk.callgetKonanieDetail(1206);
        LOG.info("Retval getKonanieDetail = " + ret_val_gk);

    }
}
