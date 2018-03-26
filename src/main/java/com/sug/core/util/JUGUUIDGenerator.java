package com.sug.core.util;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import java.util.Random;
import java.util.UUID;

public class JUGUUIDGenerator {
    private static class SingletonTimeBasedGenerator {
        private static final TimeBasedGenerator UUID_GENERATOR = Generators.timeBasedGenerator();
    }

    private JUGUUIDGenerator() {
    }

    public static String generate() {
        return SingletonTimeBasedGenerator.UUID_GENERATOR.generate().toString().replaceAll("-","");
    }

    public static String generateShort(Integer digit){
        int first = new Random(10).nextInt(8) + 1;
        String result = generate();
        return first + result.substring(0,digit).toUpperCase();
    }
}
