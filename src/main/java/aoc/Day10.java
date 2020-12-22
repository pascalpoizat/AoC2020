package aoc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Day10 implements Day<Integer> {

    private static final String REGEX = ".*";
    private static final String DELIM = "[\\n]+";
    private static final String FORMAT1 = "%s%s: %d x %d = %d";
    private static final String FORMAT2 = "%s%s: %d";

    private static Day<Integer> instance;

    private Day10() {
    }

    public static Day<Integer> instance() {
        if (instance == null) {
            instance = new Day10();
        }
        return instance;
    }

    @Override
    public int day() {
        return 10;
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
    public Generator<Integer> generator() {
        return x -> IntegerGenerator.instance().apply(x);
    }

    @Override
    public Predicate<Integer> filter() {
        return x -> true;
    }

    /**
     * vérifie si un arrangement d'adaptateurs est correct
     */
    public boolean estValide(List<Integer> jolts) {
        if (jolts.isEmpty()) return false;
        for (int i = 1; i < jolts.size(); i++)
            if (jolts.get(i) - jolts.get(i-1) > 3) return false;
        return true;
    }

    /**
     * calcule le différentiel d'un arrangement d'adaptateurs
     */
    public Map<Integer, Integer> differentiel(List<Integer> jolts) {
        final Map<Integer, Integer> differences = new HashMap<>();
        if (!jolts.isEmpty()) {
            int last = jolts.get(0);
            for (int i = 1; i < jolts.size(); i++) {
                differences.computeIfAbsent(jolts.get(i) - last, k -> 0);
                differences.computeIfPresent(jolts.get(i) - last, (k, v) -> v + 1);
                last = jolts.get(i);
            }
        }
        List.of(1,2,3).forEach(k -> differences.computeIfAbsent(k, x -> 0));
        return differences;
    }

    /**
     * calcule le nombre de solutions valides
     * la taille de xs doit être au moins de 2
     */
    public static final long calcule(List<Integer> xs, Map<Pair<Integer,Integer>,Long> memory, Pair<Integer,Integer> rang) {
        int dernier = rang.fst();
        int courant = rang.snd();
        // memorisation
        Pair<Integer,Integer> pAvec = new Pair<>(courant,courant+1);
        Pair<Integer,Integer> pSans = new Pair<>(dernier,courant+1);
        if(memory.containsKey(rang)) return memory.get(rang);
        // sinon
        if (courant == 0) return calcule(xs, memory, pSans);
        if (xs.get(courant) - xs.get(dernier) > 3) {
            memory.put(rang,0L);
            return 0;
        }
        if (courant == xs.size()-2) {
            memory.put(rang,1L);
            return 1;
        }
        if(!memory.containsKey(pAvec)) {
            calcule(xs, memory, pAvec);
        }
        long nbAvec = memory.get(pAvec);
        if (!memory.containsKey(pSans)) {
            calcule(xs, memory, pSans);
        }
        long nbSans = memory.get(pSans);
        memory.put(rang, nbAvec + nbSans);
        return nbAvec + nbSans;
    }

    public final Consumer<List<Integer>> postb = xs -> {        
        long nombre = calcule(xs, new HashMap<>(), new Pair<>(0,0));
        System.out.println(String.format(FORMAT2, day(), "b", nombre));
    };

    public final Consumer<List<Integer>> posta = xs -> {
        if (!estValide(xs)) {
            System.out.println("invalid adapter list");
        } else {
            final Map<Integer, Integer> differences = differentiel(xs);
            System.out.println(String.format(FORMAT1, day(), "a", differences.get(1), differences.get(3),
                    differences.get(1) * differences.get(3)));
        }
    };

    public final Consumer<List<Integer>> prepost = xs -> {
        xs.sort(Integer::compare);
        xs.add(0, 0); // power outlet
        xs.add(xs.get(xs.size() - 1) + 3); // machine input
    };

    @Override
    public Consumer<List<Integer>> postActions() {
        return prepost.andThen(posta).andThen(postb);
    }

}