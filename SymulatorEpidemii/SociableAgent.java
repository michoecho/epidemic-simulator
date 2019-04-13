package SymulatorEpidemii;

public class SociableAgent extends Agent {
    public SociableAgent(int id, double meetingChance) {
        super(id, meetingChance);
    }

    @Override
    public Agent chooseMeeting() {
        Agent neigh = getRandomNeighbor();
        if (isInfected()) return neigh;
        Agent choice = neigh.getRandomNeighbor();
        return choice == this ? neigh : choice;
    }

    @Override
    public boolean wantsToMeet() {
        return Rng.nextDouble() < getMeetingChance();
    }

    @Override
    public String getName() {
        return "towarzyski";
    }
}
