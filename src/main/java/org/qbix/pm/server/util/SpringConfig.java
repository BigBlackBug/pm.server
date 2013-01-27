package org.qbix.pm.server.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.Mongo;

@Configuration
public class SpringConfig {

	static final String MONGO_DB_NAME = "pm";

	public static ApplicationContext SPRING_CTX = new AnnotationConfigApplicationContext(
			SpringConfig.class);
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName){
		return (T) SPRING_CTX.getBean(beanName);
	}

	public @Bean
	MongoDbFactory mongoDbFactory() throws Exception {
		return new SimpleMongoDbFactory(new Mongo(), MONGO_DB_NAME);
	}

	public @Bean
	MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		return mongoTemplate;
	}

}
