package com.cdw.gatekeeper;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
class GateKeeper {
    public static void main(String[] args) {
        SpringApplication.run(GateKeeper.class, args);
    }
}
