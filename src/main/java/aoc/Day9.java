package aoc;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Day9 implements Day<Long> {

    private static final String REGEX = ".*";
    private static final String DELIM = "[\\n]+";
    private static final int WINDOW = 25;

    private static Day<Long> instance;

    private Day9() {
    }

    public static Day<Long> instance() {
        if (instance == null) {
            instance = new Day9();
        }
        return instance;
    }

    @Override
    public int day() {
        return 9;
    }

    @Override
    public String delimiter() {
        return DELIM;
    }

    @Override
    public Pattern pattern() {
        return Pattern.compile(REGEX);
    }

    @Override
    public Generator<Long> generator() {
        return x -> LongGenerator.instance().apply(x);
    }

    @Override
    public Predicate<Long> filter() {
        return x -> true;
    }

    private static final boolean correct(List<Long> xs, int i, int low, int high) {
        // exits x \in [low, high[
        // exists y \in ]low, high]
        // xs[x] != xs[y] /\ xs[i] = xs[x] + xs[y]
        final long target = xs.get(i);
        for (int x = low; x < high; x++) {
            for (int y = low + 1; y <= high; y++) {
                final long vx = xs.get(x);
                final long vy = xs.get(y);
                if ((vx != vy) && ((vx + vy) == target)) {
                    return true;
                }
            }
        }
        return false;
    }

    private final Optional<Long> findFirstIncorrect(List<Long> xs) {
        long[] window = new long[WINDOW];
        Optional<Long> nombre = Optional.empty();
        if (xs.size() < WINDOW) {
            System.out.println(day() + "a: " + "not enough numbers");
        } else {
            for (int i = 0; i < WINDOW; i++) {
                window[i] = xs.get(i);
            }
            int low = 0;
            int high = WINDOW - 1;
            for (int i = WINDOW; i < xs.size(); i++) {
                if (!correct(xs, i, low, high)) {
                    nombre = Optional.of(xs.get(i));
                    break;
                } else {
                    low++;
                    high++;
                }
            }
        }
        return nombre;
    }

    private static final Optional<Pair<Integer, Integer>> findWindow(List<Long> xs, Long x) {
        for (int i = 0; i < (xs.size() - 1); i++) {
            for (int j = i + 1; j < xs.size(); j++) {
                if (somme(xs, i, j) == x) {
                    return Optional.of(new Pair<>(i, j));
                }
            }
        }
        return Optional.empty();
    }

    private static final long somme(List<Long> xs, int i, int j) {
        long somme = 0;
        for (int k = i; k <= j; k++) {
            somme += xs.get(k);
        }
        return somme;
    }

    private static final long min(List<Long> xs, Pair<Integer, Integer> window) {
        long min = xs.get(window.fst());
        for (int i = window.fst()+1; i<=window.snd(); i++) {
            if (xs.get(i) < min) {
                min = xs.get(i);
            }
        }
        return min;
    }
    
    private static final long max(List<Long> xs, Pair<Integer, Integer> window) {
        long max = xs.get(window.fst());
        for (int i = window.fst()+1; i<=window.snd(); i++) {
            if (xs.get(i) > max) {
                max = xs.get(i);
            }
        }
        return max;
    }

    public Consumer<List<Long>> postb = xs -> {
        Optional<Pair<Integer, Integer>> window = findFirstIncorrect(xs).flatMap(x -> findWindow(xs, x));
        if (window.isPresent()) {
            long min = min(xs, window.get());
            long max = max(xs, window.get());
            System.out.println(day() + "b: " + (min + max));
        } else {
            System.out.println(day() + "b: " + "not found");
        }
    };

    public Consumer<List<Long>> posta = xs -> {
        final Optional<Long> nombre = findFirstIncorrect(xs);
        System.out.println(day() + "a: " + nombre.map(n -> n.toString()).orElse("not found"));
    };

    @Override
    public Consumer<List<Long>> postActions() {
        return posta.andThen(postb);
    }

}
