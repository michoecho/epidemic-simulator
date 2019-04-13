package SymulatorEpidemii;

public class AgentFactory implements RandomFactory<Agent> {
    private int id = 1;
    private double sociableChance;
    private double meetingChance;
    public AgentFactory(double meetingChance, double sociableChance) {
        this.meetingChance = meetingChance;
        this.sociableChance = sociableChance;
    }
    @Override
    public Agent randomNew() {
        if (Rng.nextDouble() < sociableChance) {
            return new SociableAgent(id++, meetingChance);
        } else {
            return new RegularAgent(id++, meetingChance);
        }
    }
}
