package com.transcoderG.activeMQ;


import java.io.IOException;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.transcoderG.client.TranscoderGClient;
import com.transcoderG.redis.TranscoderGRedisCommand;

@Component
@PropertySource("classpath:application.properties")
public class TranscoderGMQReceiver {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    public ActiveMQProperties activeMQProperties;
	
	@Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;
	
	private TranscoderGRedisCommand redisCommand;
	
	@Value("${activemq.name}")
	public String activemqName;
	
	/**
	 * activeMQ에서 가져온 명령어를 가지고 FFMPEG 실행 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void receiveMessage() throws JsonParseException, JsonMappingException, IOException {
		
		log.info("receiveMessage start {} {}", activeMQProperties.getBrokerUrl(), activemqName);
		ConnectionFactory factory = null;
		Connection connection = null;
		Session session = null;
		Destination destination = null;
		MessageConsumer consumer = null;
		
		try {
			// ConnectionFactory -> Connection -> Session -> Destination -> MessageConsumer -> receive()
			factory = new ActiveMQConnectionFactory(activeMQProperties.getBrokerUrl());
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(activemqName);
			consumer = session.createConsumer(destination);
			
			// http://activemq.apache.org/how-can-i-support-priority-queues.html
			// https://docs.oracle.com/cd/E19798-01/821-1841/bncfb/index.html
			while (true) {
				Message message = consumer.receive();
			    if (message != null) { 
			        if (message instanceof TextMessage) { 
			            TextMessage text = (TextMessage) message;
			            String correlation_ID = text.getJMSCorrelationID();
			            String command = text.getText();
			            //int priority = text.getJMSPriority();
			            log.info("correlation_ID={} command={}", correlation_ID, command);
			            TranscoderGClient client = new TranscoderGClient(correlation_ID, command, valueOps, redisCommand);
			            client.run();
			        } 
			    }
			}			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}	
}
