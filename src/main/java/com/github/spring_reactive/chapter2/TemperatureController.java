package com.github.spring_reactive.chapter2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.netty.http.server.HttpServerRequest;

@RestController
public class TemperatureController {    //구독자를 받는 역할을 수행

    private final TemperatureSensor temperatureSensor;

    public TemperatureController(TemperatureSensor temperatureSensor) {
        this.temperatureSensor = temperatureSensor;
    }

    @GetMapping("/temperature-stream")
    public SseEmitter event(HttpServerRequest request){
        RxSeeEmitter emitter = new RxSeeEmitter();  //구독자 session 생성

        temperatureSensor.temperatureStream()
                .subscribe(emitter.getSubscriber());    //구독

        return emitter;
    }

}
