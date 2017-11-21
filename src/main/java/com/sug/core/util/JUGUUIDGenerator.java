package com.sug.core.util;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

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
}
