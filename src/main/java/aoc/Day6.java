package aoc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Day6 implements Day<Pair<Integer, Map<Integer, Integer>>> {

    private static final String REGEX = ".*";
    private static final String DELIM = "\\n\\n";

    private static Day<Pair<Integer, Map<Integer, Integer>>> instance;

    private Day6() {
    }

    public static Day<Pair<Integer, Map<Integer, Integer>>> instance() {
        if (instance == null) {
            instance = new Day6();
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
        return 6;
    }

    @Override
    public Generator<Pair<Integer, Map<Integer, Integer>>> generator() {
        return x -> {
            Map<Integer, Integer> map = new HashMap<>();
            int nb = 0;
            for (String part : x.split("\n")) {
                nb++;
                part.chars().forEach(c -> {
                    if (map.keySet().contains(c)) {
                        map.put(c, (Integer) map.get(c) + 1);
                    } else {
                        map.put(c, 1);
                    }
                });
            }
            return Optional.of(new Pair<>(nb, map));
        };
    }

    @Override
    public Predicate<Pair<Integer, Map<Integer, Integer>>> filter() {
        return x -> true;
    }

    @Override
    public Consumer<List<Pair<Integer, Map<Integer, Integer>>>> postActions() {
        return posta.andThen(postb);
    }

    private final Consumer<List<Pair<Integer, Map<Integer, Integer>>>> posta = xs -> {
        System.out.println(day() + "a: " + xs.stream().map(Pair::snd).map(m -> m.keySet().size()).reduce(0, Integer::sum));
    };

    private final Consumer<List<Pair<Integer, Map<Integer, Integer>>>> postb = xs -> {
        long rtr = 0;
        for (Pair<Integer,Map<Integer, Integer>> p : xs) {
            long local = 0;
            for (Integer k : p.snd().keySet()) {
                if (p.snd().get(k).equals(p.fst())) {
                    local ++;
                }
            }
            rtr += local;
        }
        System.out.println(day() + "b: " + rtr);
    };
}
