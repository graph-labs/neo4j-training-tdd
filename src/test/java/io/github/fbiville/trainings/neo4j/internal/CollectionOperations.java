package io.github.fbiville.trainings.neo4j.internal;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class CollectionOperations {

    public static <T> void forEachOrderedPartition(int partitionSize,
                                                   List<T> items,
                                                   Consumer<Collection<T>> action) {
        int size = items.size();
        for (int i = 0; i < max(1, size / partitionSize); i++) {
            int start = i * partitionSize;
            action.accept(items.subList(start, min(size, start + partitionSize)));
        }
    }
}
