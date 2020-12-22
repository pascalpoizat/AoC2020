package aoc;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherGenerator implements Generator<Matcher> {

    private Pattern pattern;

    public MatcherGenerator(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public Optional<Matcher> apply(String x) {
        Matcher matcher = pattern.matcher(x);
        return (matcher.matches()) ? Optional.of(matcher) : Optional.empty();
    }
    
}
