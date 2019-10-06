package com.github.spring_reactive.chapter2;

import org.springframework.stereotype.Service;
import rx.Observable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class TemperatureSensor {

    private final Random rnd = new Random();
    private final Observable<Temperature> dataStream =  //게시자 담당
            Observable
                    .range(0, Integer.MAX_VALUE) // 0부터 Integer Max까지의 범위
                    .concatMap(tick -> Observable   //floatMap 과 비슷하지만 cancatMap은 순서를 유지하면 return 한다
                            .just(tick)     //그냥
                            .delay(rnd.nextInt(5000), TimeUnit.MILLISECONDS) //5초 이내의 랜덤 딜레이
                            .map(tickValue -> this.probe()))    //probe 실행
                    .publish()    //모든 구독자(SSE 클라이언트)에서 브로드캐스팅하는 연산자
                    .refCount();  //구독자가 없을때 센서 탐색 X

    private Temperature probe() {
        return new Temperature(16 + rnd.nextGaussian() * 10);
    }

    Observable<Temperature> temperatureStream() {
        return dataStream;
    }

}
