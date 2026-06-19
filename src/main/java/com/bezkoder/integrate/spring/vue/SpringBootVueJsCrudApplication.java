package com.bezkoder.integrate.spring.vue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class SpringBootVueJsCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootVueJsCrudApplication.class, args);
	}
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
}
