package com.sap.resource;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@ComponentScan("com.sap.*")
@EnableAsync
public class Application {
	private static final int QUEUE_CAPACITY = 10;
	private static final int MAX_POOL_SIZE = 2;
	private static final int CORE_POOL_SIZE = 2;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(CORE_POOL_SIZE);
		executor.setMaxPoolSize(MAX_POOL_SIZE);
		executor.setQueueCapacity(QUEUE_CAPACITY);
		executor.setThreadNamePrefix("Download Thread");
		executor.initialize();
		return executor;
	}

}
