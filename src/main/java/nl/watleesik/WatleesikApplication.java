package nl.watleesik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class WatleesikApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(WatleesikApplication.class, args);
    }

}

