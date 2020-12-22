package aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Programme {
    private List<Instruction> instructions;

    public Programme() {
        this.instructions = new ArrayList<>();
    }

    public void ajouter(Instruction instruction) {
        this.instructions.add(instruction);
    }

    public int taille() {
        return instructions.size();
    }

    public boolean isInBounds(int pc) {
        return pc >= 0 && pc < taille();
    }

    public Optional<Instruction> instruction(int pc) {
        if (isInBounds(pc))
            return Optional.of(instructions.get(pc));
        else
            return Optional.empty();
    }

    public void update(int pc, Instruction i) {
        if (isInBounds(pc))
            instructions.set(pc, i);
    }

    public Programme copy() {
        Programme prog = new Programme();
        instructions.forEach(i -> prog.ajouter(i.copy()));
        return prog;
    }
}
