package aoc;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Day3 implements Day<String> {

    private static final String REGEX = ".*";
    private static final String DELIM = "[\\n]+";

    private static Day<String> instance;

    private Day3() {
    }

    public static Day<String> instance() {
        if (instance == null) {
            instance = new Day3();
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
        return 3;
    }

    @Override
    public Generator<String> generator() {
        return x -> Optional.of(x);
    }

    @Override
    public Predicate<String> filter() {
        return x -> true;
    }

    @Override
    public Consumer<List<String>> postActions() {
        return posta.andThen(postb);
    }

    private final Consumer<List<String>> postb = xs -> {
        int[][] slopes = { { 1, 1 }, { 3, 1 }, { 5, 1 }, { 7, 1 }, { 1, 2 } };
        long res = 1;
        for (int[] slope : slopes) {
            res *= day3help(xs, slope[0], slope[1]);
        }
        System.out.println(day() + "b: " + res);
    };

    private final Consumer<List<String>> posta = xs -> System.out.println(day() + "a: " + day3help(xs, 3, 1));

    private static final int day3help(List<String> xs, int dx, int dy) {
        final char TREE = '#';
        int nbTrees = 0;
        int row = 0;
        int col = 0;
        row += dy;
        col += dx;
        while (row < xs.size()) {
            if (xs.get(row).charAt(col % 31) == TREE) {
                nbTrees++;
            }
            row += dy;
            col += dx;
        }
        return nbTrees;
    }
}
