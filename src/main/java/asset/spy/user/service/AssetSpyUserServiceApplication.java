package asset.spy.user.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "asset.spy.user.service",
        "asset.spy.auth.lib"})
public class AssetSpyUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssetSpyUserServiceApplication.class, args);
    }
}