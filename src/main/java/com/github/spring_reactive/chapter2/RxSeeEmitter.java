package com.github.spring_reactive.chapter2;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import rx.Subscriber;

import java.io.IOException;


@Slf4j
class RxSeeEmitter extends SseEmitter { //온도 이벤트에 대한 구독자 캡슐화 및 Message Broker 담당

    static final long SSE_SESSION_TIMEOUT = 30 * 60 * 1000L;
    private final Subscriber<Temperature> subscriber;   //구독자


    RxSeeEmitter(){
        super(SSE_SESSION_TIMEOUT);

        this.subscriber = new Subscriber<Temperature>() {   //reactivestream과는 별개의 개념으로 1.3버젼에서는 onSubscribe가 없음
            @Override
            public void onCompleted() { //stream 완료
            }

            @Override
            public void onError(Throwable e) {  //stream error handle
            }

            @Override
            public void onNext(Temperature temperature) {   //SSE 클라이언트에서 신호 전달
                try{
                    RxSeeEmitter.this.send(temperature);
                }catch (IOException e) {
                    log.error("Session 끊김");
                    unsubscribe();      //데이터 전달 실패시 구독 취소
                }
            }
        };

        //unsubscribe는 2. version에서는 dispose로 변경됨
        onCompletion(subscriber::unsubscribe);  //세션 완료 작업
        onTimeout(subscriber::unsubscribe);     //세션 timeout 작업

    }

    Subscriber<Temperature> getSubscriber(){    //구독 가입자 노출
        return subscriber;
    }

}
