package com.dxc.integration.microservice;

import com.dxc.integration.microservice.runner.StreamRunner;
import com.dxc.integration.microservice.init.StreamInitializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@ComponentScan(basePackages = "com.dxc")

public class integraciaApplication implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(integraciaApplication.class);

    private final StreamRunner streamRunner;

    private final StreamInitializer streamInitializer;


    public integraciaApplication(StreamRunner runner, StreamInitializer initializer) {
        this.streamRunner = runner;
        this.streamInitializer = initializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(integraciaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("App starts...");
        streamInitializer.init();
        streamRunner.start();
    }
}
