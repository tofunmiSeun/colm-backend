package com.tofunmi.colm.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication(scanBasePackages = {"com.tofunmi.mitri"})
@EnableMongoRepositories(basePackages = {"com.tofunmi.mitri"})
@EnableWebSocket
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
