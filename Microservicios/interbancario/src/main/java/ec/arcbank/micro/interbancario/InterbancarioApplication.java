package ec.arcbank.micro.interbancario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class InterbancarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterbancarioApplication.class, args);
    }
}
