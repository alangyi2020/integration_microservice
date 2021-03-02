package com.dxc.integration.microservice.runner.impl.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dxc.common_tools.kafka.resultsProducer;
import com.dxc.hibernate.sqlQuery;
import com.dxc.soapcall.vyhladajKonanie;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

//import org.apache.kafka


import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

@Service
@Scope("prototype")
public class VyhladajKonanie implements Runnable{
	
	String name;
	String bootstrapServer = "192.168.80.131:9092";
    String groupId = "vyhladajkonanie";
    String topic = "konanie";
    
    private static String myServiceID;
    private static String serviceId;
    
    private static KafkaConsumer<String, String> consumer;
	
	private static final Logger LOG = LoggerFactory.getLogger(VyhladajKonanie.class);
	public void setName(String name){
		this.name = name;
	}
	
	@Override

	public void run() {
		
		System.out.println(name + " is running");
		
		LOG.info("creating thread " + name);
		/*CountDownLatch latch = new CountDownLatch(1);
        final Runnable myConsumerRunnable;
        myConsumerRunnable = new ConsumerRunnable(bootstrapServer,
                groupId,
                topic,
                latch);
        Thread myThread = new Thread(myConsumerRunnable);
        myThread.start();
        Runtime.getRuntime().addShutdownHook( new Thread( new Runnable() {
			public void run() {
			    LOG.info("caugh shutdown hook");
			    ((ConsumerRunnable) myConsumerRunnable).shutdown();
			        }
		})

        );*/
		
		sqlQuery myQuery = new sqlQuery();
		myServiceID = myQuery.getQueryResults("select distinct sp.Service_Provider_id from soapdemo.Service_Provider sp where sp.method_name = 'vyhladajKonanie'");
		
		//System.out.println("mmmmm = " + myServiceID);
		serviceId = myServiceID;
		
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer );
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName() );
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()  );
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		
		
		
		//create consumer
		consumer = new KafkaConsumer<String, String>(properties);
				
		//subscribe to consumer
		KafkaConsumer<String, String> consumer =new KafkaConsumer<String, String>(properties);
		//consumer.subscribe(Arrays.asList(topic,"integration_start"));
		consumer.subscribe(Arrays.asList(topic));
		
		resultsProducer producerComplete = new resultsProducer(bootstrapServer, "vyhladajKonanie_Complete");
		
		
		while(true) {
			ConsumerRecords<String, String> records =  consumer.poll(Duration.ofMillis(100));
			for (ConsumerRecord<String, String> record : records) {
				
				LOG.info("Key: " + record.key() + ", Value: " + record.value());
				LOG.info("Partition: " + record.partition() + ", Offset: " + record.offset());
				try {
					int myId;
					myId = Integer.parseInt(serviceId);
					
					//doCallService ds = new doCallService();
					//TimeUnit.SECONDS.sleep(30);
					vyhladajKonanie sc = new vyhladajKonanie();
					myQuery = new sqlQuery();
					myQuery.update("insert into soapdemo.Request set request_id  = "+ record.value() + ", request_datetime  = NOW(), CSRU_Service_id = 1, Service_Provider_id = " + myId);
					
					//System.out.println("Service returned ===== " + ds.do_callSOAPvyhladajKonanie(Integer.parseInt(record.value()),1);
					String soapResult = sc.callVyhladajKonanie(Integer.parseInt(record.value()));
					LOG.info("Soapcall vyhladajKonanie returned value: " + soapResult);
					
					producerComplete.produceToTopic(soapResult + "|1");
					
					sc = null;
					myQuery = null;
					
					//System.out.println("Service returned ===== " + ds.doCallStartProcessing(14));
				} catch (Exception e) {
					LOG.error(e.toString());
					//consumer.close();
				} finally {
					
				//HibernateUtil.shutdown(); 
				}
				
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				
	                    
			}
		}
		
		
		
		
	}
	
	/*public class ConsumerRunnable implements Runnable {
        private CountDownLatch latch;
        private KafkaConsumer<String, String> consumer;
        private Logger logger = LoggerFactory.getLogger(ConsumerRunnable.class.getName());

        public ConsumerRunnable(String bootstrapServer,
                                String groupId,
                                String topic,
                                CountDownLatch latch) {
            this.latch = latch;

            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,groupId);
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

            consumer = new KafkaConsumer<String, String>(properties);

            //subscribe consumer to topic

            consumer.subscribe(Arrays.asList(topic));
        }
        public void run() {
        	
            try {
                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, String> record : records) {
                        logger.info("Key = " + record.key() + ", Value = " + record.value());
                        int myId;
    					myId = 1;
    					
    					//doCallService ds = new doCallService();
    					//TimeUnit.SECONDS.sleep(30);
    					vyhladajKonanie sc = new vyhladajKonanie();
    					sqlQuery myQuery = new sqlQuery();
    					//myQuery.update("insert into soapdemo.Request set request_id  = "+ record.value() + ", request_datetime  = NOW(), CSRU_Service_id = 1, Service_Provider_id = " + myId);
    					myQuery.update("insert into soapdemo.Request set request_id  = "+ record.value() + ", request_datetime  = NOW(), CSRU_Service_id = 1, Service_Provider_id = " + myId);
    					
    					//System.out.println("Service returned ===== " + ds.do_callSOAPvyhladajKonanie(Integer.parseInt(record.value()),1);
    					try {
    					String soapResult = sc.callVyhladajKonanie(Integer.parseInt(record.value()));
    					LOG.info("Soapcall vyhladajKonanie returned value: " + soapResult);
    					
    					String bootstrapServers= "192.168.80.131:9092";;
						resultsProducer producerComplete = new resultsProducer(bootstrapServers, "vyhladajKonanie_Complete");
						producerComplete.produceToTopic(soapResult + "|1");
    					} catch (Exception e) {
    						logger.info(e.toString());
    					} finally {
	    					myQuery = null;
	    					sc = null;

    					}
                        
                        
                    }
                }
            } catch(WakeupException e) {
                logger.info("Received shutdown signal");
            } finally {
                consumer.close();
                latch.countDown();
            }

        }

        public void shutdown() {
            consumer.wakeup();

        }
    }*/

}
