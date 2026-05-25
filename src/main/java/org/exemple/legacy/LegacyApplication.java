package org.exemple.legacy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class LegacyApplication {

    public static void main(String[] args) {
        SpringApplication.run(LegacyApplication.class, args);
    }

}
