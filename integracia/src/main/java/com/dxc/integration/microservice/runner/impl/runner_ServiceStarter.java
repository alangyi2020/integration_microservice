package com.dxc.integration.microservice.runner.impl;

import com.dxc.integration.microservice.config.AppConfig;
import com.dxc.integration.microservice.runner.StreamRunner;
import com.dxc.integration.microservice.runner.impl.service.GetKonanieDetail;
import com.dxc.integration.microservice.runner.impl.service.GetResult;
import com.dxc.integration.microservice.runner.impl.service.VyhladajKonanie;

import java.io.Closeable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component

public class runner_ServiceStarter extends StreamRunner {
	private static final Logger LOG = LoggerFactory.getLogger(runner_ServiceStarter.class);
	
	
	@Override
    
    public void start() throws Exception {
		ApplicationContext context = new AnnotationConfigApplicationContext(
				AppConfig.class);

		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context
				.getBean("taskExecutor");

		VyhladajKonanie vyhladajKonanie = (VyhladajKonanie) context.getBean("vyhladajKonanie");
		vyhladajKonanie.setName("VyhladajKonanie");
		taskExecutor.execute(vyhladajKonanie);
		
		GetKonanieDetail getKonanieDetail  = (GetKonanieDetail) context.getBean("getKonanieDetail");
		getKonanieDetail.setName("GetKonanieDetail");
		taskExecutor.execute(getKonanieDetail);
		
		GetResult getResult = (GetResult) context.getBean("getResult");
		getResult.setName("GetResult");
		taskExecutor.execute(getResult);
		


		for (;;) {
			int count = taskExecutor.getActiveCount();
			System.out.println("Active Threads : " + count);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (count == 0) {
				taskExecutor.shutdown();
				
				break;
			}
		}

    }

	
	}







