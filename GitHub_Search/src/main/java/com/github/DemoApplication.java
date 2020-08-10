package com.github;

import com.github.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@SpringBootApplication
public class DemoApplication {

    @Autowired
    public UserRepository userRepository;

    /**
     * LogInSession function
     * @return new LogInSession() for get instance
     */
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public LogInSession logIn(){return new LogInSession();}
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
