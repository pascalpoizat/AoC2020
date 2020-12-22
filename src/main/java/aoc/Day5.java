package aoc;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Day5 implements Day<Seat> {

    private static final String REGEX = ".*";
    private static final String DELIM = "[\\n]+";

    private static Day<Seat> instance;

    private Day5() {
    }

    public static Day<Seat> instance() {
        if (instance == null) {
            instance = new Day5();
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
        return 5;
    }

    @Override
    public Generator<Seat> generator() {
        return Day5::toSeat;
    }

    @Override
    public Predicate<Seat> filter() {
        return x -> true;
    }

    @Override
    public Consumer<List<Seat>> postActions() {
        return posta.andThen(postb);
    }

    private static final Optional<Seat> toSeat(String code) {
        // FBFBBFF - RLR
        // 0101100 - 101
        // 4+8+32 = 44 - 1+4 = 5
        String regex = "([FB]{7})([RL]{3})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(code);
        if (!matcher.matches())
            return Optional.empty();
        String part1 = matcher.group(1).replace('F', '0').replace('B', '1');
        String part2 = matcher.group(2).replace('L', '0').replace('R', '1');
        int row = Integer.parseInt(part1, 2);
        int col = Integer.parseInt(part2, 2);
        return Optional.of(new Seat(row, col));
    }

    private final Consumer<List<Seat>> posta = xs -> {
        int maxId = xs.stream().map(Seat::id).max(Integer::compareTo).orElse(0);
        System.out.println(day() + "a: " + maxId);
    };

    private final Consumer<List<Seat>> postb = xs -> {
        List<Integer> sortedSeatIds = xs.stream().map(Seat::id).sorted().collect(Collectors.toList());
        for (int i = 0; i < sortedSeatIds.size(); i++) {
            if (i > 0 && sortedSeatIds.get(i) != sortedSeatIds.get(i - 1) + 1)
                System.out.println(day() + "b: " + sortedSeatIds.get(i - 1) + 1);
        }
    };

}
