package com.github.spring_reactive;

import com.github.spring_reactive.toby1.PubSub;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringReactiveApplication {

    public static void main(String[] args) {
        PubSub pubSub = new PubSub();
        //SpringApplication.run(SpringReactiveApplication.class, args);
    }

}
