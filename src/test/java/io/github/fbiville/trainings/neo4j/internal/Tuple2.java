package io.github.fbiville.trainings.neo4j.internal;

import java.util.Objects;

public class Tuple2<V1, V2> {

    private final V1 value1;
    private final V2 value2;

    private Tuple2(V1 value1, V2 value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public static <V1,V2> Tuple2<V1,V2> tuple(V1 v1, V2 v2) {
        return new Tuple2<>(v1, v2);
    }

    public V1 value1() {
        return value1;
    }

    public V2 value2() {
        return value2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value1, value2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Tuple2 other = (Tuple2) obj;
        return Objects.equals(this.value1, other.value1)
            && Objects.equals(this.value2, other.value2);
    }

    @Override
    public String toString() {
        return String.format("%s, %s", value1, value2);
    }
}
