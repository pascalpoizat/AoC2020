package aoc;

import java.util.HashMap;
import java.util.Map;

public class Passport {
    private Map<String,String> fields;
    public Passport() {
        this.fields = new HashMap<>();
    }
    public String put(String k, String v) {
        return this.fields.put(k, v);
    }
    public String get(String k) {
        return this.fields.get(k);
    }
    public boolean containsKey(String k) {
        return this.fields.containsKey(k);
    }
}
