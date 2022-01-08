package com.tju.bclab.nb_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.tju.bclab"})
public class NbBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.tju.bclab.nb_backend.NbBackendApplication.class, args);
    }

}
