package aoc;

public class Dependance {
    private String from;
    private String to;
    private int qty;
    public Dependance(String from, String to, int qty) {
        this.from = from;
        this.to = to;
        this.qty = qty;
    }
    public String from() { return from; }
    public String to() { return to; }
    public int qty() { return qty; }
    @Override
    public String toString() {
        return String.format("%s --%d--> %s", from, qty, to);
    }
}
