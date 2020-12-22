package aoc;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class Day1 implements Day<Integer> {

    private static final String REGEX = ".*";
    private static final String DELIM = "[\\n]+";

    private static Day<Integer> instance;

    private static Generator<Integer> generator = IntegerGenerator.instance();

    private Day1() {
    }

    public static Day<Integer> instance() {
        if (instance == null) {
            instance = new Day1();
        }
        return instance;
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
    public int day() {
        return 1;
    }

    @Override
    public Generator<Integer> generator() {
        return generator;
    }

    @Override
    public Predicate<Integer> filter() {
        return x -> true;
    }

    @Override
    public Consumer<List<Integer>> postActions() {
        return posta.andThen(postb);
    }

    private final Consumer<List<Integer>> postb = xs -> {
        final int SEARCHED = 2020;
        for (int i = 0; i < xs.size() - 2; i++) {
            for (int j = i + 1; j < xs.size() - 1; j++) {
                for (int k = j + 1; k < xs.size(); k++) {
                    if (xs.get(i) + xs.get(j) + xs.get(k) == SEARCHED) {
                        System.out.println(day() + "b: "+xs.get(i) * xs.get(j) * xs.get(k));
                        break;
                    }
                }
            }
        }
    };

    private final Consumer<List<Integer>> posta = xs -> {
        final int SEARCHED = 2020;
        for (int i = 0; i < xs.size() - 1; i++) {
            for (int j = i + 1; j < xs.size(); j++) {
                if (xs.get(i) + xs.get(j) == SEARCHED) {
                    System.out.println(day() + "a: "+ xs.get(i) * xs.get(j));
                    break;
                }
            }
        }
    };

}
