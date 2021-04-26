package com.dxc.integration.microservice.runner.impl.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dxc.common_tools.kafka.resultsProducer;
import com.dxc.hibernate.sqlQuery;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@Scope("prototype")
public class GetResult implements Runnable{
	
	String name;
	String bootstrapServer = "127.0.0.1:9092";
    String groupId = "thread";
    String topic = "konanie";
    private static KafkaConsumer<String, String> consumer;
	
	private static final Logger LOG = LoggerFactory.getLogger(GetResult.class);
	public void setName(String name){
		this.name = name;
	}
	
	@Override

	public void run() {
		
		System.out.println(name + " is running");
		
		LOG.info("creating thread " + name);
		
sqlQuery myQuery = new sqlQuery();
		
		
		
		
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer );
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName() );
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()  );
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		
		
		
		Properties propertiesprod = new Properties();
		propertiesprod.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer );
		propertiesprod.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName() );
		propertiesprod.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()  );
		
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(propertiesprod);
		
		
		//KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
		
		//create consumer
		consumer = new KafkaConsumer<String, String>(properties);
				
		//subscribe to consumer
		KafkaConsumer<String, String> consumer =new KafkaConsumer<String, String>(properties);
		consumer.subscribe(Arrays.asList("getKonanieDetail_Complete","vyhladajKonanie_Complete","getZoznamSudov_Complete"));
		//consumer.subscribe(Arrays.asList(topic));
		
		resultsProducer producerComplete = new resultsProducer(bootstrapServer, "konanie_Complete");
		
		
		while(true) {
			
			
			ConsumerRecords<String, String> records =  consumer.poll(Duration.ofMillis(100));
			for (ConsumerRecord<String, String> record : records) {
				
				LOG.info("Key: " + record.key() + ", Value: " + record.value());
				LOG.info("Partition: " + record.partition() + ", Offset: " + record.offset());
				try {
					
					
					String[] arrSplit = record.value().split("\\|");
					
					myQuery.update("commit");
					String myResultCount = myQuery.getQueryResults("select count(*) FROM soapdemo.Response b  where b.request_id = "+ arrSplit[2] );
					System.out.println("myResultCount ===== " + myResultCount);
					
					String myServiceP = myQuery.getQueryResults("select count(*) from soapdemo.Service_Provider");
					System.out.println("myServiceP ===== " + myServiceP);
							
					//System.out.println("Service returned ===== " + soapResult);
					if(myServiceP.equals(myResultCount)) {
						System.out.println(record.value() + " finished...");
						producerComplete.produceToTopic(record.value() + " finished...");
						producer.flush();					
						
						//flush and close
						//producer.close();

					} else {
						System.out.println(record.value() + " not complete...");
						producerComplete.produceToTopic(record.value() + " not complete..." + myResultCount + ", " + myServiceP + ", " + arrSplit[2]);
						producer.flush();
					}
					//producerComplete.produceToTopic(record.value() + " finished...");
					;
				} catch (Exception e) {
					LOG.error(e.toString());
					//producer.close();
				} finally {
					
				//HibernateUtil.shutdown(); 
						
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
							//consumer.close();
						}
				}
				
				
	                    
			}
		}
		
		
		
		
		
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
		
		
		
		//System.out.println(name + " is running");

	}
	
/*	public class ConsumerRunnable implements Runnable {
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

            consumer.subscribe(Arrays.asList("getKonanieDetail_Complete","vyhladajKonanie_Complete"));
        }
        public void run() {
        	
            try {
                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, String> record : records) {
                        logger.info("Key = " + record.key() + ", Value = " + record.value());
                        try {
                        
	                        Properties propertiesprod = new Properties();
	                		String bootstrapServers ="127.0.0.1:9092";
							propertiesprod.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers  );
	                		propertiesprod.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName() );
	                		propertiesprod.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()  );
	                		
	                		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(propertiesprod);
	                		String[] arrSplit = record.value().split("\\|");
	                		
	                		sqlQuery myQuery = new sqlQuery();
	    					myQuery.update("commit");
	    					String myResultCount = myQuery.getQueryResults("select count(*) FROM soapdemo.Response b  where b.request_id = "+ arrSplit[2] );
	    					System.out.println("myResultCount ===== " + myResultCount);
	    					
	    					String myServiceP = myQuery.getQueryResults("select count(*) from soapdemo.Service_Provider");
	    					System.out.println("myServiceP ===== " + myServiceP);
	    					resultsProducer producerComplete = new resultsProducer(bootstrapServers, "konanie_Complete");
	    							
	    					//System.out.println("Service returned ===== " + soapResult);
	    					if(myServiceP.equals(myResultCount)) {
	    						System.out.println(record.value() + " finished...");
	    						producerComplete.produceToTopic(record.value() + " finished...");
	    						producer.flush();					
	    						
	    						//flush and close
	    						//producer.close();
	
	    					} else {
	    						System.out.println(record.value() + " not complete...");
	    						producerComplete.produceToTopic(record.value() + " not complete..." + myResultCount + ", " + myServiceP + ", " + arrSplit[2]);
	    						producer.flush();
	    					}
	                    
	                        
	    					} catch (Exception e) {
	    						System.out.println(e.toString());
	    					} finally {
	    						
	    					//HibernateUtil.shutdown(); 
	    					} 
                        
                        
                    }
                
            } } catch(WakeupException e) {
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