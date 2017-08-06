package com.github.evgdim.rxjava;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Scheduler;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;

import java.util.Arrays;

public class CreateTests {
    private final Logger logger = LoggerFactory.getLogger(SubscribeTests.class);
    @Test
    public void create() {
        Observable.just("some val").subscribe(logger::info);
    }
    @Test
    public void merge() {
        Observable<Integer> first = Observable.from(Arrays.asList(1, 2, 3));
        Observable<Integer> second = Observable.from(Arrays.asList(4, 5, 6));
        Observable.merge(first, second).subscribe(i ->logger.info(String.valueOf(i)));
    }

    @Test
    public void stillSync() {
        Observable<Integer> first = Observable.from(Arrays.asList(1, 2, 3));
        Observable<Integer> second = Observable.from(Arrays.asList(4, 5, 6));
        Observable.merge(first, second)
                .subscribeOn(Schedulers.computation())
                .subscribe(i ->logger.info(String.valueOf(i)));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void async() {
        Observable<Integer> first = Observable.from(Arrays.asList(1, 2, 3)).subscribeOn(Schedulers.computation());
        Observable<Integer> second = Observable.from(Arrays.asList(4, 5, 6)).subscribeOn(Schedulers.computation());
        Observable.merge(first, second)
                .subscribe(i ->logger.info(String.valueOf(i)));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
