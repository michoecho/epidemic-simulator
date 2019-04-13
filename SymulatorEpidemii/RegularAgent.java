package SymulatorEpidemii;

public class RegularAgent extends Agent {

    public RegularAgent(int id, double meetingChance) {
        super(id, meetingChance);
    }

    @Override
    public Agent chooseMeeting() {
        return getRandomNeighbor();
    }

    @Override
    public boolean wantsToMeet() {
        return Rng.nextDouble() <= getMeetingChance() * (getState() == HealthState.INFECTED ? 0.5 : 1);
    }

    @Override
    public String getName() {
        return "zwykły";
    }
}
