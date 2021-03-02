package com.dxc.common_tools.kafka;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class resultsProducer {
	private Logger logger;
	private String bootstrapServers;
	private String topic;
	private Properties properties;
	private KafkaProducer<String, String> producer;
	private ProducerRecord<String,String> record;
	
	public resultsProducer(String bootstrapServers, String topic) {
		
		this.bootstrapServers = bootstrapServers;
		this.topic = topic;
		logger = LoggerFactory.getLogger(resultsProducer.class);
		
		// TODO Auto-generated method stub
		System.out.println("Portal producer");
		
		//create properties
		properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers );
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName() );
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()  );
		
		
	}
	
	public void produceToTopic(String message) throws InterruptedException, ExecutionException {
		producer = new KafkaProducer<String, String>(properties);
		record = new ProducerRecord<String,String>(topic, "id", message);
		producer.send(record, new Callback() { 
		    public void onCompletion(RecordMetadata recordMetadata, Exception e) {  
		        if (e== null) {  
		            logger.info("Successfully received the details as: \n" +  
		                    "Topic: " + recordMetadata.topic() + "\n" +  
		                    "Partition: " + recordMetadata.partition() + "\n" +  
		                    "Offset " + recordMetadata.offset() + "\n" +  
		                    "Timestamp " + recordMetadata.timestamp());  
		                      }  
		  
		         else {  
		            logger.error("Can't produce,getting error",e);  
		  
		        }  
		    }  
		}).get(); 
	
		//flush data
		producer.flush();
		
		//flush and close
		producer.close();
		 
	}
	

}
