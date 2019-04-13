package SymulatorEpidemii;

public class Meeting implements Comparable<Meeting> {

    private Agent agent;
    private int day;

    public Agent getAgent() {
        return agent;
    }

    public int getDay() {
        return day;
    }

    public Meeting(Agent agent, int day) {
        this.agent = agent;
        this.day = day;
    }

    @Override
    public int compareTo(Meeting meeting) {
        return Integer.compare(day, meeting.day);
    }

}
