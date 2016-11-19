package io.github.fbiville.trainings.neo4j.internal;

import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamOperations {

    public static <T> Stream<T> create(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T> Stream<T> create(T[] array) {
        return Arrays.stream(array);
    }
}
