package io.github.fbiville.trainings.neo4j.internal;

import java.util.Iterator;

public class IteratorOperations {

    public static long size(Iterator<?> result) {
        long count = 0;
        while (result.hasNext()) {
            count++;
            result.next();
        }

        return count;
    }
}
