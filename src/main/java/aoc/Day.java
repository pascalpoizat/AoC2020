package aoc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Day<T> {

    int day();

    String delimiter();

    Pattern pattern();

    Generator<T> generator();

    Predicate<T> filter();

    Consumer<List<T>> postActions();

    String PATH = "/Users/pascalpoizat/Documents/Code/AoC2020/src/main/resources";

    static final String FNF = "File Not Found";

    default void run() {
        final Optional<List<T>> oxs = fromFile();
        if (oxs.isPresent()) {
            postActions().accept(oxs.get());
        }
    }

    default Optional<List<T>> fromFile() {
        try (Scanner scanner = new Scanner(new File(String.format("%s/day%d.txt", PATH, day())))) {
            StringBuilder sb = new StringBuilder();
            while(scanner.hasNextLine()) {
                sb.append(scanner.nextLine()+"\n");
            }
            final String[] parts = sb.toString().split(delimiter());
            final List<T> values = Stream.of(parts)
                .map(generator())
                .flatMap(Optional::stream)
                .filter(filter())
                .collect(Collectors.toList());
            return Optional.ofNullable(values);
        } catch (final FileNotFoundException e) {
            System.out.println(FNF + e);
            return Optional.empty();
        }
    }
}
