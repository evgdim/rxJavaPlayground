package com.github.evgdim.rxjava;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Scheduler;
import rx.Single;
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
    public void stillSync1() {
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

    @Test
    public void async() {
        Observable<Integer> first = Observable.from(Arrays.asList(1, 2, 3)).subscribeOn(Schedulers.computation());
        Observable<Integer> second = Observable.from(Arrays.asList(4, 5, 6)).subscribeOn(Schedulers.computation());

        first.subscribe(i ->logger.info(String.valueOf(i)));
        second.subscribe(i ->logger.info(String.valueOf(i)));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void single() {
        Single<Object> first = Single.create(singleSubscriber -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                singleSubscriber.onError(e);
            }
            singleSubscriber.onSuccess("first");
        }).subscribeOn(Schedulers.computation());

        Single<Object> second = Single.create(singleSubscriber -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                singleSubscriber.onError(e);
            }
            singleSubscriber.onSuccess("second");
        }).subscribeOn(Schedulers.computation());

        first.subscribe(s -> logger.info(s.toString()));
        second.subscribe(s -> logger.info(s.toString()));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void singleCombine() {
        Single<Object> first = Single.create(singleSubscriber -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                singleSubscriber.onError(e);
            }
            logger.info("in first");
            singleSubscriber.onSuccess("first");
        }).subscribeOn(Schedulers.computation());

        Single<Object> second = Single.create(singleSubscriber -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                singleSubscriber.onError(e);
            }
            logger.info("in second");
            singleSubscriber.onSuccess("second");
        }).subscribeOn(Schedulers.computation());

//        first.subscribe(s -> logger.info(s.toString()));
//        second.subscribe(s -> logger.info(s.toString()));

        Observable<Object> merge = Single.merge(first, second);
        merge.subscribe(s -> logger.info(s.toString()));
        try {
            Thread.sleep(3000);
            logger.info("Finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
