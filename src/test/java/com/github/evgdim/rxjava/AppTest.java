package com.github.evgdim.rxjava;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Observer;

import java.util.Arrays;

public class AppTest {
    private final Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Test
    public void basic() {
        Observable
                .from(Arrays.asList(1,2,3,4,5,6))
                .subscribe(i -> logger.info(String.valueOf(i)),
                        e -> logger.error("error "+e),
                        () -> logger.info("Done"));
    }
}
