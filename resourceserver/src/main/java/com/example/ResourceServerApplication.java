package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@EnableResourceServer
public class ResourceServerApplication {

    @Controller
    public static class WebController {
        @RequestMapping("/")
        public ResponseEntity<String> indexPage() {

            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.TEXT_PLAIN);

            ResponseEntity<String> response = new ResponseEntity<String>("This message comes from res server", header, HttpStatus.OK);
            return response;
        }

        @RequestMapping("/hello.html")
        public String indexPageHtml() {
            return "classpath*:/templates/index.jsp";
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }

}
