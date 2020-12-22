package aoc;

public enum SeatPosition {
    FLOOR, EMPTY, OCCUPIED;

    @Override
    public String toString() {
        return switch (this) {
            case FLOOR -> ".";
            case EMPTY -> "L";
            case OCCUPIED -> "#";
        };
    }
}