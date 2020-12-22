package aoc;

import java.util.Optional;

public class LongGenerator implements Generator<Long> {
    private static final String NFE = "Number Format Exception";
    private static Generator<Long> instance;

    private LongGenerator() {}

    public static Generator<Long> instance() {
        if (instance == null) {
            instance = new LongGenerator();
        }
        return instance;
    }

    @Override
    public Optional<Long> apply(String x) {
        try {
            final long rtr = Long.parseLong(x);
            return Optional.of(rtr);
        } catch (NumberFormatException e) {
            System.out.println(NFE + e);
            return Optional.empty();
        }
    }
}
