package com.example.webdemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@Slf4j
public class BigDecimalTest {

    @Test
    @DisplayName(value = "Double precision test.")
    public void testDoublePrecision() {
        String original = "220000107.64283134";
        double parseDouble = Double.parseDouble(original);
        log.info("parseDouble value = {}", parseDouble);
        BigDecimal bigDecimal = new BigDecimal(original);
        double deserialized = bigDecimal.setScale(18).doubleValue();
        log.info("deserialized value = {}", deserialized);
        log.info("bigDecimal value = {}", bigDecimal);
    }
}
