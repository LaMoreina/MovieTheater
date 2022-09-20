package com.jpmc.theater.model;

import com.jpmc.theater.model.LocalDateProvider;
import org.junit.jupiter.api.Test;

public class LocalDateProviderTest {
    @Test
    void makeSureCurrentTime() {
        System.out.println("current time is - " + LocalDateProvider.singleton().currentDate());
    }
}
