package trafficsim;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "trafficsim")
public class WebApp {
    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
    }

    @Bean
    public CommandLineRunner runSimulation(FlowControl flowControl) {
        return args -> {
            //FlowControl flowControl = new FlowControl();
            flowControl.start();
        };
    }
}