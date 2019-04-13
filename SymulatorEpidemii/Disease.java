package SymulatorEpidemii;

public class Disease {
    private final double lethality;
    private final double curability;
    private final double virulence;

    public Disease(double lethality, double curability, double virulence) {
        this.lethality = lethality;
        this.curability = curability;
        this.virulence = virulence;
    }

    public boolean isCured() {
        return Rng.nextDouble() <= curability;
    }

    public boolean isContagious() {
        return Rng.nextDouble() <= virulence;
    }

    public boolean isTerminal() {
        return Rng.nextDouble() <= lethality;
    }
}
