package com.kmbl.tax;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.ExecutorServiceManager;
import org.apache.camel.spi.ThreadPoolProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.kmbl.tax.camel.routebuilder.SftpPollerRouteBuilder;

@SpringBootApplication
public class KmblConverterUtilityApplication {

	private static final Logger logger = LoggerFactory.getLogger(KmblConverterUtilityApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(KmblConverterUtilityApplication.class, args);
		CamelContext context = new DefaultCamelContext();
		try {
			logger.info(" KmblConverterUtilityApplication Started......");
			ExecutorServiceManager manager = context.getExecutorServiceManager();
			ThreadPoolProfile profile = manager.getDefaultThreadPoolProfile();
			profile.setMaxPoolSize(50);

			context.addRoutes(new SftpPollerRouteBuilder());
			logger.info("  Calling to the SftpPollerRouteBuilder from the KmblConverterUtilityApplication class");
			context.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
