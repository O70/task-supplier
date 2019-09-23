package org.thraex.supplier.webflux;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thraex.supplier.common.util.ResponseData;

/**
 * @author 鬼王
 * @date 2019/09/23 17:19
 */
@SpringBootApplication
@RestController
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value("${spring.application.name}")
    private String application;

    @GetMapping("/task")
    public ResponseData task() {
        return ResponseData.success("WebFlux");
    }

}
