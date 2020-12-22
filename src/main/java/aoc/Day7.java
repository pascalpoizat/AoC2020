package aoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day7 implements Day<Pair<String, List<Dependance>>> {

    private static final String REGEX = ".*";
    private static final String DELIM = "[\\n]+";
    private static final String REGEX_DEP1 = "(.*) contain (.*)";
    private static final String REGEX_DEP2 = "\s*([\\d]+) (.*)";

    private static final String SEARCHED = "shiny gold bag";

    private static Day<Pair<String, List<Dependance>>> instance;

    private Day7() {
    }

    public static Day<Pair<String, List<Dependance>>> instance() {
        if (instance == null) {
            instance = new Day7();
        }
        return instance;
    }

    @Override
    public int day() {
        return 7;
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
    public Generator<Pair<String, List<Dependance>>> generator() {
        return x -> {
            Pattern pattern1 = Pattern.compile(REGEX_DEP1);
            Pattern pattern2 = Pattern.compile(REGEX_DEP2);
            Matcher matcher1 = pattern1.matcher(x);
            Pair<String, List<Dependance>> deps;
            if (matcher1.matches()) {
                final String from = cleanName(matcher1.group(1));
                deps = new Pair<>(from, new ArrayList<>());
                String tos = matcher1.group(2);
                tos = tos.substring(0, tos.length() - 1); // remove ending "."
                Stream.of(tos.split(",")).forEach(dep -> {
                    Matcher matcher2 = pattern2.matcher(dep);
                    if (matcher2.matches()) {
                        Optional<Integer> qty = IntegerGenerator.instance().apply(matcher2.group(1));
                        String to = cleanName(matcher2.group(2));
                        deps.snd().add(new Dependance(from, to, qty.orElse(0)));
                    }
                });
                return Optional.of(deps);
            } else {
                return Optional.empty();
            }
        };
    }

    @Override
    public Predicate<Pair<String, List<Dependance>>> filter() {
        return x -> true;
    }

    @Override
    public Consumer<List<Pair<String, List<Dependance>>>> postActions() {
        return posta.andThen(postb);
    }

    private final Consumer<List<Pair<String, List<Dependance>>>> postb = xs -> {
        Map<String, List<Dependance>> deps = genDependencyMap(xs, Dependance::from, Function.identity());
        int total = compute2(deps, SEARCHED);
        System.out.println(day() + "b: " + (total - 1));
    };

    private final Consumer<List<Pair<String, List<Dependance>>>> posta = xs -> {
        Map<String, List<String>> deps = genDependencyMap(xs, Dependance::to, Dependance::from);
        List<String> trans = compute1(deps, SEARCHED);
        System.out.println(day() + "a: " + (trans.size() - 1));
    };

    private static final int compute2(Map<String, List<Dependance>> deps, String root) {
        if (!deps.containsKey(root)) return 1;
        int sum = 1;
        for (Dependance dep : deps.get(root)) {
            sum += dep.qty() * compute2(deps, dep.to());
        }
        return sum;
    }

    private static final List<String> compute1(Map<String, List<String>> deps, String root) {
        Set<String> front = new HashSet<>();
        Set<String> treated = new HashSet<>();
        List<String> rtr = new ArrayList<>();
        front.add(root);
        while (!front.isEmpty()) {
            String current = front.stream().findFirst().get();
            front.remove(current);
            if (!treated.contains(current)) {
                treated.add(current);
                rtr.add(current);
                if (deps.containsKey(current)) {
                    deps.get(current).forEach(x -> {
                        front.add(x);
                    });
                }
            }
        }
        return rtr;
    }

    private static final <T, U> Map<T, List<U>>
    genDependencyMap(List<Pair<String, List<Dependance>>> xs,
                     Function<Dependance, T> f,
                     Function<Dependance, U> g) {
        Map<T, List<U>> deps = new HashMap<>();
        xs.stream()
            .map(Pair::snd)
            .flatMap(List::stream)
            .forEach(d -> deps.computeIfAbsent(f.apply(d), k -> new ArrayList<>())
                                .add(g.apply(d)));
        return deps;
    }

    private static final String cleanName(String name) {
        name = name.strip();
        if (name.endsWith("s")) {
            name = name.substring(0, name.length() - 1);
        }
        return name;
    }

}
