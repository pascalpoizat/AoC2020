package aoc;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Day2 implements Day<Matcher> {

    private static final String REGEX = "(\\d+)-(\\d+) ([a-z]): ([a-z]+)";
    private static final String DELIM = "[\\n]+";

    private static Day<Matcher> instance;

    private Day2() {
    }

    public static Day<Matcher> instance() {
        if (instance == null) {
            instance = new Day2();
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
        return 2;
    }

    @Override
    public Generator<Matcher> generator() {
        return new MatcherGenerator(REGEX);
    }

    @Override
    public Predicate<Matcher> filter() {
        return x -> true;
    }

    @Override
    public Consumer<List<Matcher>> postActions() {
        return posta.andThen(postb);
    }

    private static final Predicate<Matcher> predb = x -> {
        int index1 = Integer.parseInt(x.group(1));
        int index2 = Integer.parseInt(x.group(2));
        char letter = x.group(3).charAt(0);
        String password = x.group(4);
        int found = 0;
        if (password.charAt(index1 - 1) == letter) {
            found++;
        }
        if (password.charAt(index2 - 1) == letter) {
            found++;
        }
        return (found == 1);
    };

    private static final Predicate<Matcher> preda = x -> {
        long min = Long.parseLong(x.group(1));
        long max = Long.parseLong(x.group(2));
        char letter = x.group(3).charAt(0);
        String password = x.group(4);
        long times = password.chars().filter(c -> c == letter).count();
        return (min <= times && times <= max);
    };

    private final Consumer<List<Matcher>> posta = xs -> System.out.println(day() + "a: "+ xs.stream().filter(preda).count());

    private final Consumer<List<Matcher>> postb = xs -> System.out.println(day() + "b: "+ xs.stream().filter(predb).count());

}
