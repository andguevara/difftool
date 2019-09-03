package com.aguevara.difftool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.aguevara.difftool.repositories")
@SpringBootApplication
public class DifftoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(DifftoolApplication.class, args);
    }

}
