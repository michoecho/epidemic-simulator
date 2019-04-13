package SymulatorEpidemii;

import java.util.ArrayList;
import java.util.PriorityQueue;

public abstract class Agent implements Vertex<Agent> {
    private PriorityQueue<Meeting> plans = new PriorityQueue<>();
    private ArrayList<Agent> neighbors = new ArrayList<>();
    private Body body = new Body();
    private int id;
    private double meetingChance;

    abstract public boolean wantsToMeet();

    abstract public Agent chooseMeeting();

    abstract public String getName();

    public Agent (int id, double meetingChance) {
        this.id = id;
        this.meetingChance = meetingChance;
    }

    @Override
    public ArrayList<Agent> getNeighbors() {
        return neighbors;
    }

    @Override
    public void removeNeighbor(Agent v) {
        neighbors.remove(v);
    }

    @Override
    public void addNeighbor(Agent v) {
        neighbors.add(v);
    }

    private void die() {
        plans.clear();
        for (Agent a : neighbors) {
            a.removeNeighbor(this);
        }
        neighbors.clear();
    }

    private Body getBody() {
        return body;
    }

    public void updateHealth() {
        body.update();
        if (getState() == HealthState.DEAD)
            die();
    }

    public double getMeetingChance() {
        return meetingChance;
    }

    public void planMeetings(Calendar calendar) {
        if (getNeighbors().isEmpty() || calendar.daysLeft() <= 0) return;
        while (wantsToMeet()) {
            Agent neigh = chooseMeeting();
            plans.add(new Meeting(neigh, calendar.getRandomFutureDay()));
        }
    }

    public final void forceInfect(Disease disease) {
        body.forceInfect(disease);
    }

    private void meet(Agent a) {
        body.contactWith(a.getBody());
    }

    public void attendMeetings(Calendar calendar) {
        while (!plans.isEmpty() && plans.peek().getDay() == calendar.getCurrentDay()) {
            Agent partner = plans.poll().getAgent();
            meet(partner);
            partner.meet(this);
        }
    }

    public HealthState getState() {
        return body.getState();
    }

    public final boolean isInfected() {
        return getState() == HealthState.INFECTED;
    }

    public final int getId() {
        return id;
    }
}
