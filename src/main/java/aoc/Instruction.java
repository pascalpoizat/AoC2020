package aoc;

import java.util.Optional;

public class Instruction {
    public enum IType {
        ACC, JMP, NOP;

        public static Optional<IType> of(String val) {
            if (val.toLowerCase().equals("acc")) return Optional.of(ACC);
            if (val.toLowerCase().equals("jmp")) return Optional.of(JMP);
            if (val.toLowerCase().equals("nop")) return Optional.of(NOP);
            return Optional.empty();
        }
    }

    private IType type;
    private int value;

    public IType type() {
        return type;
    }

    public void setType(IType type) {
        this.type = type;
    }

    public int value() {
        return value;
    }

    public Instruction(IType type, int value) {
        this.type = type;
        this.value = value;
    }

    public void executer(Pair<Integer, Integer> reg) {
        switch (type) {
            case ACC:
                reg.setFst(reg.fst() + 1);
                reg.setSnd(reg.snd() + value);
                break;
            case JMP:
                reg.setFst(reg.fst() + value);
                break;
            case NOP:
            default:
                reg.setFst(reg.fst() + 1);
        }
    }

    public Instruction copy() {
        return new Instruction(type, value);
    }
}
