package aoc;

public class Execution {
    private static final String FORMAT = "execution (%d) -> pc: %d, value: %d, finished: %s, loops: %s";

    private Programme prog;
    private boolean[] executed;
    private Pair<Integer, Integer> state; // pc x value

    public Execution(Programme prog) {
        this.prog = prog;
        initialize();
    }

    public Execution run() {
        initialize();
        while (!loops() && isInBounds()) {
            final Instruction i = prog.instruction(pc()).get();
            executed[pc()] = true;
            i.executer(state);
        }
        return this;
    }

    public int pc() {
        return state.fst();
    }
    
    public int value() {
        return state.snd();
    }
    
    public boolean isInBounds() {
        return prog.isInBounds(pc());
    }

    public boolean loops() {
        return isInBounds() && executed[pc()];
    }    

    public boolean isFinished() {
        return pc() == prog.taille();
    }

    private void initialize() {
        state = new Pair<>(0, 0);
        this.executed = new boolean[prog.taille()];
        for (int i = 0; i < prog.taille(); i++) {
            executed[i] = false;
        }
    }

    @Override
    public String toString() {
        return String.format(FORMAT, prog.taille(), pc(), value(), isFinished(), loops());
    }
}
