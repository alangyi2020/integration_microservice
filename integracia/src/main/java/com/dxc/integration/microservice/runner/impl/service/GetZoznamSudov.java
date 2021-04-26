package com.dxc.integration.microservice.runner.impl.service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dxc.common_tools.kafka.resultsProducer;
import com.dxc.hibernate.sqlQuery;
import com.dxc.soapcall.getKonanieDetail;
import com.dxc.soapcall.getZoznamSudov;

// /home/Kafka/kafka/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic getZoznamSudov_Complete



@Service
@Scope("prototype")
public class GetZoznamSudov implements Runnable{
	
	String name;
	String bootstrapServer = "127.0.0.1:9092";
    String groupId = "getZoznamSudov";
    String topic = "konanie";
    
    private static String myServiceID;
    private static String serviceId;
	private static KafkaConsumer<String, String> consumer;
    
	
	private static final Logger LOG = LoggerFactory.getLogger(GetKonanieDetail.class);
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
		myServiceID = myQuery.getQueryResults("select distinct sp.Service_Provider_id from soapdemo.Service_Provider sp where sp.method_name = 'getZoznamSudov'");
		
		System.out.println("mmmmm = " + myServiceID);
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
		
		resultsProducer producerComplete = new resultsProducer(bootstrapServer, "getZoznamSudov_Complete");
		
		
		while(true) {
			ConsumerRecords<String, String> records =  consumer.poll(Duration.ofMillis(100));
			for (ConsumerRecord<String, String> record : records) {
				
				LOG.info("Key: " + record.key() + ", Value: " + record.value());
				LOG.info("Partition: " + record.partition() + ", Offset: " + record.offset());
				
					int myId;
					myId = Integer.parseInt(serviceId);
					
					getZoznamSudov sc = new getZoznamSudov();
					myQuery = new sqlQuery();
					LOG.info("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA = " + record.value());
					LOG.info("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB = " + myId);
					LOG.info("insert into soapdemo.Request set request_id  = "+ record.value() + ", request_datetime  = NOW(), CSRU_Service_id = 1, Service_Provider_id = " + myId);
					myQuery.update("insert into soapdemo.Request set request_id  = "+ record.value() + ", request_datetime  = NOW(), CSRU_Service_id = 1, Service_Provider_id = " + myId);
					
					try {
					String soapResult = sc.callgetZoznamSudov(Integer.parseInt(record.value()));
					//System.out.println("Service returned ===== " + soapResult);
					LOG.info("Soapcall getZoznamSudov returned value: " + soapResult);
					producerComplete.produceToTopic(soapResult + "|2");
					
					sc = null;
					myQuery = null;
					//System.out.println("Service returned ===== " + ds.doCallStartProcessing(14));
				} catch (Exception e) {
					LOG.error(e.toString());
					//consumer.close();
				} finally {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				//HibernateUtil.shutdown(); 
				}
				
				
	                    
			}
		}
		
		
		
		

	}
	


}
