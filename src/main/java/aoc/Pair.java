package aoc;

import java.util.Objects;

public class Pair<T, U> {
    private T fst;
    private U snd;

    public Pair(T fst, U snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public T fst() {
        return fst;
    }

    public U snd() {
        return snd;
    }

    public void setFst(T val) {
        this.fst = val;
    }

    public void setSnd(U val) {
        this.snd = val;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair pair = (Pair) o;
        return Objects.equals(fst, pair.fst) && Objects.equals(snd, pair.snd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fst, snd);
    }

}
