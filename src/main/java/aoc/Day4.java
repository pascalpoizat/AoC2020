package aoc;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Optional;

public class Day4 implements Day<Passport> {

    private static final String REGEX = ".*";
    private static final String DELIM = "\\n\\n";
    private static final String REGEX_FIELD = "([a-z]{3}):(.*)";
    private static final String DELIM_FIELD = "[\\s]+";

    private static Day<Passport> instance;

    private Day4() {
    }

    public static Day<Passport> instance() {
        if (instance == null) {
            instance = new Day4();
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
        return 4;
    }

    @Override
    public Generator<Passport> generator() {
        return x -> {
            Passport p = new Passport();
            Pattern pattern = Pattern.compile(REGEX_FIELD);
            Scanner scanner = new Scanner(x);
            scanner.useDelimiter(DELIM_FIELD);
            while(scanner.hasNext(pattern)) {
                Matcher matcher = pattern.matcher(scanner.next(pattern));
                if (matcher.matches()) {
                    p.put(matcher.group(1), matcher.group(2));
                }
            }
            scanner.close();
            return Optional.of(p);
        };
    }

    @Override
    public Predicate<Passport> filter() {
        return x -> true;
    }

    @Override
    public Consumer<List<Passport>> postActions() {
        return posta.andThen(postb);
    }

    private final Consumer<List<Passport>> posta = xs -> {
        System.out.println(day() + "a: " + xs.stream().filter(x -> isCorrectPassport(x, false)).count());
    };
    
    private final Consumer<List<Passport>> postb = xs -> {
        System.out.println(day() + "b: " + xs.stream().filter(x -> isCorrectPassport(x, true)).count());
    };

    private static final Map<String, Boolean> PASSFIELDS = Map.of("byr", true, "iyr", true, "eyr", true, "hgt", true,
            "hcl", true, "ecl", true, "pid", true, "cid", false);

    private static final boolean yearValidation(String x, int min, int max) {
        String regex = "(\\d{4})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(x);
        if (!matcher.matches())
            return false;
        int year = Integer.parseInt(x);
        return (min <= year && year <= max);
    }

    private static final Predicate<String> byrValidation = x -> yearValidation(x, 1920, 2002);
    private static final Predicate<String> iyrValidation = x -> yearValidation(x, 2010, 2020);
    private static final Predicate<String> eyrValidation = x -> yearValidation(x, 2020, 2030);
    private static final Predicate<String> hgtValidation = x -> {
        String regex = "([0-9]+)(in|cm)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(x);
        if (!matcher.matches())
            return false;
        int val = Integer.parseInt(matcher.group(1));
        if (matcher.group(2).equals("cm")) {
            return (150 <= val && val <= 193);
        } else {
            return (59 <= val && val <= 76);
        }
    };
    private static final Predicate<String> hclValidation = x -> {
        String regex = "[#]([0-9a-f]{6})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(x);
        return matcher.matches();
    };
    private static final Predicate<String> eclValidation = x -> {
        return Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(x);
    };
    private static final Predicate<String> pidValidation = x -> {
        String regex = "(\\d{9})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(x);
        return matcher.matches();
    };
    private static final Predicate<String> cidValidation = x -> true;

    private static final Map<String, Predicate<String>> VALIDATORS = Map.of("byr", byrValidation, "iyr", iyrValidation,
            "eyr", eyrValidation, "hgt", hgtValidation, "hcl", hclValidation, "ecl", eclValidation, "pid",
            pidValidation, "cid", cidValidation);

    private static final boolean isCorrectPassport(Passport passport, boolean withValidation) {
        for (String key : PASSFIELDS.keySet()) {
            if (PASSFIELDS.get(key) && !passport.containsKey(key))
                return false;
            if (withValidation && !VALIDATORS.get(key).test(passport.get(key)))
                return false;
        }
        return true;
    }

}
