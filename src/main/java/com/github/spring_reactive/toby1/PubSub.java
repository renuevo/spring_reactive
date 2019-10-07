package com.github.spring_reactive.toby1;

//org.reactivestreams 는 spring webflux에 포함되어 추가됨

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public class PubSub {

    public PubSub() {
        Iterable<Integer> iter = Arrays.asList(1, 2, 3, 4, 5);

        Publisher publisher = (s) -> {
            Iterator<Integer> it = iter.iterator();
            s.onSubscribe(new Subscription() {  //Subscription이 backpressure
                boolean complete = true;

                @Override
                public void request(long n) {   //요청 허용 개수
                    try {
                        while (n-- > 0) {
                            if (it.hasNext()) {
                                s.onNext(it.next());
                            } else {
                                if (complete) {
                                    s.onComplete();
                                    complete = false;
                                }
                                break;
                            }
                        }
                    } catch (RuntimeException e) {
                        s.onError(e);
                    }
                }

                @Override
                public void cancel() {      //

                }
            });
        };

        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {   // 필수   Subscription이 backpressure
                System.out.println("onSubscribe");
                this.subscription = s;
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer integer) {       // 0 ~ 무제한
                System.out.println("onNext : " + integer);
                subscription.request(2);
            }

            @Override
            public void onError(Throwable t) {  //에러 처리
                System.out.println("onError : " + t.getLocalizedMessage());
            }

            @Override
            public void onComplete() {  //완료
                System.out.println("onComplete");
            }
        };


        publisher.subscribe(subscriber);
    }

}
