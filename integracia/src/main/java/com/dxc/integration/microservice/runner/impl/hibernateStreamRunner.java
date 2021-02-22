package com.dxc.integration.microservice.runner.impl;

import com.dxc.hibernate.HibernateUtil;
import com.dxc.hibernate.sqlQuery;
import com.dxc.integration.microservice.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component

public class hibernateStreamRunner extends StreamRunner {
    private static final Logger LOG = LoggerFactory.getLogger(hibernateStreamRunner.class);
    @Override
    public void start() throws Exception {
        sqlQuery myQuery = new sqlQuery();
        String myResultCount = myQuery.getQueryResults("select count(*) FROM soapdemo.Response b  where b.request_id = 1" );
        System.out.println("myResultCount ===== " + myResultCount);
        HibernateUtil.shutdown();
    }
}
