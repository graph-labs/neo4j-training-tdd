package io.github.fbiville.trainings.neo4j.internal;

import java.util.Optional;
import java.util.stream.Stream;

public class OptionalOperations {

    public static <V> Stream<V> flatMap(Optional<V> optional) {
        return optional.isPresent() ? Stream.of(optional.get()) : Stream.<V>empty();
    }

}
