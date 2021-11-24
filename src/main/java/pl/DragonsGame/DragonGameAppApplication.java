package pl.DragonsGame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DragonGameAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(DragonGameAppApplication.class, args);
    }
}
