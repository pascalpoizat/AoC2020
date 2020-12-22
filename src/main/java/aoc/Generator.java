package aoc;

import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface Generator<T> extends Function<String,Optional<T>> {
    Optional<T> apply(String x);
}
