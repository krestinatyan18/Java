package com.example.demo;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVProcessorTest {

    @Test
    void testCalculateAverage() {
        CSVProcessor csvProcessor = new CSVProcessor();

        assertEquals(0.0, csvProcessor.calculateAverage(List.of()));

        assertEquals(5.0, csvProcessor.calculateAverage(List.of(5.0)));

        assertEquals(3.0, csvProcessor.calculateAverage(List.of(1.0, 2.0, 3.0, 4.0, 5.0)));

    }
}
