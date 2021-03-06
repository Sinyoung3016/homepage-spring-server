package com.inspire12.homepage;

import com.inspire12.homepage.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class HomepageApplication {

	public static void main(String[] args) throws IOException {
        SpringApplication application = new SpringApplication(HomepageApplication.class);
        application.addListeners(new ApplicationPidFileWriter());
        application.run(args);
	}
}

