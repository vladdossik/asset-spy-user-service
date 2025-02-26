package asset.spy.user.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AssetSpyUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssetSpyUserServiceApplication.class, args);
    }
}