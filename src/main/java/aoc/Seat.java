package aoc;

public class Seat {
    private int row;
    private int col;

    public Seat(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int id() {
        return row * 8 + col;
    }
}
