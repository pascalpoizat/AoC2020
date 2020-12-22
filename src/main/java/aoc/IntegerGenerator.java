package aoc;

import java.util.Optional;

public class IntegerGenerator implements Generator<Integer> {
    private static final String NFE = "Number Format Exception";
    private static Generator<Integer> instance;

    private IntegerGenerator() {}

    public static Generator<Integer> instance() {
        if (instance == null) {
            instance = new IntegerGenerator();
        }
        return instance;
    }

    @Override
    public Optional<Integer> apply(String x) {
        try {
            final int rtr = Integer.parseInt(x);
            return Optional.of(rtr);
        } catch (NumberFormatException e) {
            System.out.println(NFE + e);
            return Optional.empty();
        }
    }
}
