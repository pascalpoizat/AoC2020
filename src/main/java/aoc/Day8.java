package aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Supplier;

import aoc.Instruction.IType;

public class Day8 implements Day<Instruction> {

    private static final String REGEX = ".*";
    private static final String DELIM = "[\\n]+";
    private static final String REGEX_INSTR = "([a-z]{3}) ([+-])([\\d]+)";
    private static final String FORMAT = "%s%s: %s";
    private static final Set<IType> mutationPoints = Set.of(IType.NOP, IType.JMP);
    private static final UnaryOperator<IType> mutationFunction =  it -> switch (it) {
        case JMP -> IType.NOP;
        case NOP -> IType.JMP;
        case ACC -> IType.ACC;};

    private static Day<Instruction> instance;

    private Day8() {
    }

    public static Day<Instruction> instance() {
        if (instance == null) {
            instance = new Day8();
        }
        return instance;
    }

    @Override
    public int day() {
        return 8;
    }

    @Override
    public String delimiter() {
        return DELIM;
    }

    @Override
    public Pattern pattern() {
        return Pattern.compile(REGEX);
    }

    @Override
    public Generator<Instruction> generator() {
        return x -> {
            Pattern pattern = Pattern.compile(REGEX_INSTR);
            Matcher matcher = pattern.matcher(x);
            if (matcher.matches()) {
                Optional<IType> otype = IType.of(matcher.group(1));
                int sign = matcher.group(2).equals("-") ? (-1) : 1;
                Optional<Integer> ovalue = IntegerGenerator.instance().apply(matcher.group(3));
                if (otype.isEmpty() || ovalue.isEmpty())
                    return Optional.empty();

                return Optional.of(new Instruction(otype.get(), sign * ovalue.get()));
            } else {
                return Optional.empty();
            }
        };
    }

    @Override
    public Predicate<Instruction> filter() {
        return x -> true;
    }

    public class ProgrammeSupplier implements Supplier<Optional<Programme>> {

        private Programme prog;
        int rangErreur = -1;

        public ProgrammeSupplier(Programme prog) {
            this.prog = prog;
        }

        @Override
        public Optional<Programme> get() {
            Programme rtr = prog.copy();
            for (int pc = rangErreur + 1; pc < rtr.taille(); pc++) {
                Instruction i = rtr.instruction(pc).get();
                if (i.type() == IType.JMP) {
                    i.setType(IType.NOP);
                    rangErreur = pc;
                    return Optional.of(rtr);
                }
                if (i.type() == IType.NOP) {
                    i.setType(IType.JMP);
                    rangErreur = pc;
                    return Optional.of(rtr);
                }
            }
            return Optional.empty();
        }
    }

    public static final List<Integer> mutationPoints(Programme p, Set<IType> targets) {
        List<Integer> rtr = new ArrayList<>();
        for (int pc = 0; pc < p.taille(); pc++)
            if (p.instruction(pc).filter(i -> targets.contains(i.type())).isPresent())
                rtr.add(pc);
        return rtr;
    }

    public static final Programme muter(Programme p, int pc, UnaryOperator<IType> f) {
        Programme mutant = p.copy();
        mutant.instruction(pc).ifPresent(i -> i.setType(f.apply(i.type())));
        return mutant;
    }

    public Consumer<List<Instruction>> postB2 = xs -> {
        final Programme prog = new Programme();
        xs.forEach(prog::ajouter);
        final Optional<Execution> exec = 
            mutationPoints(prog, mutationPoints)
            .parallelStream()
            .map(pc -> muter(prog,pc,mutationFunction))
            .map(mutant -> new Execution(mutant).run())
            .filter(Execution::isFinished)
            .findAny();
        System.out.println(String.format(FORMAT, 
            day(), "b2", exec.map(Execution::toString).orElse("not found")));
    };

    public Consumer<List<Instruction>> postB = xs -> {
        final Programme prog0 = new Programme();
        xs.forEach(prog0::ajouter);
        ProgrammeSupplier supplier = new ProgrammeSupplier(prog0);
        Optional<Programme> prog;
        prog = Optional.of(prog0);
        Execution exec;
        boolean found = false;
        boolean more = true;
        do {
            exec = new Execution(prog.get());
            exec.run();
            if (exec.isFinished())
                found = true;
            else {
                prog = supplier.get();
                more = !prog.isEmpty();
            }
        } while (!found && more);
        if (found)
            System.out.println(String.format(FORMAT, day(), "b", exec.toString()));
        else
            System.out.println(String.format(FORMAT, day(), "b", "not found"));
    };

    public Consumer<List<Instruction>> postA = xs -> {
        final Programme prog = new Programme();
        xs.forEach(prog::ajouter);
        Execution exec = new Execution(prog);
        exec.run();
        System.out.println(String.format(FORMAT, day(), "a", exec.toString()));
    };

    @Override
    public Consumer<List<Instruction>> postActions() {
        return postA.andThen(postB).andThen(postB2);
    }

}
