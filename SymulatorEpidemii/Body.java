package SymulatorEpidemii;

import static SymulatorEpidemii.HealthState.*;

public class Body {
    private HealthState state = HEALTHY;
    private Disease disease = null;

    public void update() {
        if (state != INFECTED) return;
        if (disease.isTerminal())
            state = DEAD;
        else if (disease.isCured())
            state = IMMUNE;
    }

    public void contactWith(Body b) {
        if (state != HEALTHY) return;
        if (b.isContagious()) {
            state = INFECTED;
            disease = b.getDisease();
        }
    }

    public void forceInfect(Disease d) {
        state = INFECTED;
        disease = d;
    }

    public HealthState getState() {
        return state;
    }

    public boolean isContagious() {
        return state == INFECTED && disease.isContagious();
    }

    public Disease getDisease() {
        return disease;
    }

}
