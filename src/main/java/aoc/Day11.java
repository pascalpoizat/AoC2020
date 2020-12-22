package aoc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day11 implements Day<SeatPosition[]> {

    private static final String REGEX = ".*";
    private static final String DELIM = "[\\n]+";
    private static final String FORMAT1 = "%s%s: %s";
    private static final String FORMAT2 = "%s%s: %d";
    private int size = 0;

    private static Day<SeatPosition[]> instance;

    private Day11() {
    }

    public static Day<SeatPosition[]> instance() {
        if (instance == null) {
            instance = new Day11();
        }
        return instance;
    }

    @Override
    public int day() {
        return 11;
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
    public Generator<SeatPosition[]> generator() {
        return x -> {
            if (size == 0)
                size = x.length();
            if (x.length() == size) {
                List<SeatPosition> rtr = x.chars().mapToObj(c -> (char) c).map(c -> switch (c) {
                    case '.' -> SeatPosition.FLOOR;
                    case 'L' -> SeatPosition.EMPTY;
                    default -> SeatPosition.OCCUPIED;
                }).collect(Collectors.toList());
                return Optional.of(rtr.toArray(new SeatPosition[0]));
            } else {
                return Optional.empty();
            }
        };
    }

    @Override
    public Predicate<SeatPosition[]> filter() {
        return x -> true;
    }

    public int nbOccupied(SeatPosition[][] positions, int rows, int cols) {
        int rtr = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (positions[i][j] == SeatPosition.OCCUPIED)
                    rtr++;
            }
        }
        return rtr;
    }

    public Set<Pair<Integer, Integer>> casesAutour2(SeatPosition[][] positions, int i, int j, int rows, int cols) {

    }

    public Set<Pair<Integer, Integer>> casesAutour(int i, int j, int rows, int cols) {
        Set<Pair<Integer, Integer>> cases = Set.of(new Pair<>(i - 1, j - 1), new Pair<>(i - 1, j),
                new Pair<>(i - 1, j + 1), new Pair<>(i, j + 1), new Pair<>(i + 1, j + 1), new Pair<>(i + 1, j),
                new Pair<>(i + 1, j - 1), new Pair<>(i, j - 1));
        return cases.stream().filter(p -> p.fst() >= 0 && p.fst() < rows && p.snd() >= 0 && p.snd() < cols)
                .collect(Collectors.toSet());
    }

    public void afficher(SeatPosition[][] positions, int rows, int cols) {
        System.out.print("\n");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(positions[i][j]);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public boolean condition1(SeatPosition[][] positions, int i, int j, int rows, int cols, boolean close) {
        return (close ? casesAutour(i, j, rows, cols) : casesAutour2(positions, i, j, rows, cols)).stream()
                .filter(p -> positions[p.fst()][p.snd()] == SeatPosition.OCCUPIED).count() == 0;
    }

    public boolean condition2(SeatPosition[][] positions, int i, int j, int rows, int cols, int limit, boolean close) {
        return (close ? casesAutour(i, j, rows, cols) : casesAutour2(positions, i, j, rows, cols)).stream()
                .filter(p -> positions[p.fst()][p.snd()] == SeatPosition.OCCUPIED).count() >= limit;
    }

    public void copy(SeatPosition[][] source, SeatPosition[][] target, int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                target[i][j] = source[i][j];
            }
        }
    }

    public boolean run1(SeatPosition[][] positions, int rows, int cols, int limit, boolean close) {
        boolean change = false;
        SeatPosition[][] copy = new SeatPosition[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (positions[i][j] == SeatPosition.EMPTY && condition1(positions, i, j, rows, cols, close)) {
                    copy[i][j] = SeatPosition.OCCUPIED;
                    change = true;
                } else if (positions[i][j] == SeatPosition.OCCUPIED && condition2(positions, i, j, rows, cols, limit, close)) {
                    copy[i][j] = SeatPosition.EMPTY;
                    change = true;
                } else {
                    copy[i][j] = positions[i][j];
                }
            }
        }
        if (change) copy(copy,positions,rows,cols);
        return change;
    }

    public void run(SeatPosition[][] positions, int rows, int cols, int limit, boolean close) {
        while (run1(positions, rows, cols, limit, close))
            ;
    }

    public final Consumer<List<SeatPosition[]>> postb = xs -> {
        if (!xs.isEmpty()) {
            int rows = xs.size();
            int cols = size;
            SeatPosition[][] positions = new SeatPosition[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    positions[i][j] = xs.get(i)[j];
                }
            }
            run(positions, rows, cols, 5, false);
            System.out.println(String.format(FORMAT2, day(), "b", nbOccupied(positions, rows, cols)));
        } else {
            System.out.println(String.format(FORMAT1, day(), "b", "no lines"));
        }
    };

    public final Consumer<List<SeatPosition[]>> posta = xs -> {
        if (!xs.isEmpty()) {
            int rows = xs.size();
            int cols = size;
            SeatPosition[][] positions = new SeatPosition[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    positions[i][j] = xs.get(i)[j];
                }
            }
            run(positions, rows, cols, 4, true);
            System.out.println(String.format(FORMAT2, day(), "a", nbOccupied(positions, rows, cols)));
        } else {
            System.out.println(String.format(FORMAT1, day(), "a", "no lines"));
        }
    };

    @Override
    public Consumer<List<SeatPosition[]>> postActions() {
        return posta.andThen(postb);
    }

}