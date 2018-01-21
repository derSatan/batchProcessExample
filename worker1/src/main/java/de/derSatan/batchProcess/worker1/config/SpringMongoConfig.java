package de.derSatan.batchProcess.worker1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mongodb.MongoClient;

@Configuration
@EnableScheduling
public class SpringMongoConfig {
	public @Bean
	MongoTemplate mongoTemplate() throws Exception {

		MongoTemplate mongoTemplate =
			new MongoTemplate(new MongoClient("localhost:27017"),"batch");
		return mongoTemplate;
	}
}
