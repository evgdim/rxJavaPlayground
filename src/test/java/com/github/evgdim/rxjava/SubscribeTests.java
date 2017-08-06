package com.github.evgdim.rxjava;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.Arrays;

public class SubscribeTests {
    private final Logger logger = LoggerFactory.getLogger(SubscribeTests.class);

    @Test
    public void basic() {
        Observable
                .from(Arrays.asList(1,2,3,4,5,6))
                .subscribe(i -> logger.info(String.valueOf(i)),
                        e -> logger.error("error "+e),
                        () -> logger.info("Done"));
    }

    @Test
    public void execOnAnotherThread() throws InterruptedException {
        Observable
                .from(Arrays.asList(1,2,3,4,5,6))
                .subscribeOn(Schedulers.computation())
                .subscribe(i -> logger.info(String.valueOf(i)));
        Thread.sleep(1000);
    }
}
