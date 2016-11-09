package com.transcoderG;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.transcoderG.activeMQ.TranscoderGMQReceiver;

/**
 * http://redutan.github.io/2015/09/23/spring-commandline-example
 * @author user
 *
 */
@SpringBootApplication
public class TranscoderGApplication implements CommandLineRunner {
	
	private static final Logger log = LoggerFactory.getLogger(TranscoderGApplication.class);
	
	@Autowired
	TranscoderGMQReceiver receiver;
	
	@Override
	public void run(String... args) throws Exception {
		receiver.receiveMessage();
	}
	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		log.info("TranscoderGApplication main start ...");
		SpringApplication.run(TranscoderGApplication.class, args);
	}	
}
