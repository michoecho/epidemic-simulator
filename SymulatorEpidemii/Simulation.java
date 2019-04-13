package SymulatorEpidemii;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Simulation {
    public class PropertyException extends RuntimeException {
        public final String key;
        public final String value;
        public PropertyException (String k, String v) { key = k; value = v; }
    }
    private static final String[] propStrings = {"seed", "liczbaAgentów", "prawdTowarzyski", "prawdSpotkania", "prawdZarażenia",
            "prawdWyzdrowienia", "śmiertelność", "liczbaDni", "śrZnajomych", "plikZRaportem"};
    private String[] props;
    private long seed;
    private int numOfAgents;
    private double sociableChance;
    private double meetingChance;
    private double infectionChance;
    private double cureChance;
    private double deathChance;
    private int simulationLength;
    private int averageNeighbours;
    private PrintWriter reportWriter;

    public Simulation (Properties defaultProps, Properties customProps) {
        props = new String[propStrings.length];
        for (int i = 0; i < propStrings.length; ++i)
            props[i] = defaultProps.getProperty(propStrings[i]);
        setProperties();
        for (int i = 0; i < propStrings.length; ++i)
            props[i] = customProps.getProperty(propStrings[i], props[i]);
        setProperties();
        for (int i = 0; i < propStrings.length; ++i)
            if (props[i] == null)
                throw new PropertyException(propStrings[i], null);
    }

    private void setProperties() {
        int i = -1;
        try {
            if (props[++i] != null) setSeed(Long.parseLong(props[i]));
            if (props[++i] != null) setNumOfAgents(Integer.parseInt(props[i]));
            if (props[++i] != null) setSociableChance(Double.parseDouble(props[i]));
            if (props[++i] != null) setMeetingChance(Double.parseDouble(props[i]));
            if (props[++i] != null) setInfectionChance(Double.parseDouble(props[i]));
            if (props[++i] != null) setCureChance(Double.parseDouble(props[i]));
            if (props[++i] != null) setDeathChance(Double.parseDouble(props[i]));
            if (props[++i] != null) setSimulationLength(Integer.parseInt(props[i]));
            if (props[++i] != null) setAverageNeighbours(Integer.parseInt(props[i]));
            if (props[++i] != null) setReportWriter(new PrintWriter(Files.newBufferedWriter(Paths.get(props[i]),
                    StandardCharsets.UTF_8)));
        } catch (IllegalArgumentException|IOException e) {
            throw new PropertyException(propStrings[i], props[i]);
        }
    }

    public void run() {
        reportWriter.printf("# twoje wyniki powinny zawierać te komentarze%n");
        for (int j = 0; j < propStrings.length - 1; ++j)
            reportWriter.printf("%s=%s%n", propStrings[j], props[j]);
        Rng.setSeed(seed);
        AgentFactory af = new AgentFactory(meetingChance, sociableChance);
        Graph<Agent> g = new Graph<>(numOfAgents, averageNeighbours * numOfAgents / 2, af);
        Disease flu = new Disease(deathChance, cureChance, infectionChance);
        g.getRandomVertex().forceInfect(flu);
        reportWriter.printf("%n# agenci jako: id typ lub id* typ dla chorego%n");
        for (Agent a : g)
            reportWriter.printf("%d%s %s%n", a.getId(), (a.isInfected() ? "*" : ""), a.getName());
        reportWriter.printf("%n# graf%n");
        for (Agent a : g) {
            reportWriter.printf("%d", a.getId());
            for (Agent neigh : a.getNeighbors()) {
                reportWriter.printf(" %d", neigh.getId());
            }
            reportWriter.println();
        }
        reportWriter.printf("%n# liczność w kolejnych dniach%n");
        for (Calendar calendar = new Calendar(simulationLength); !calendar.isOver(); calendar.advance()) {
            for (Agent a : g) a.updateHealth();
            for (Agent a : g) a.planMeetings(calendar);
            for (Agent a : g) a.attendMeetings(calendar);
            int healthy = 0, infected = 0, immune = 0;
            for (Agent a : g) {
                switch (a.getState()) {
                    case DEAD: break;
                    case HEALTHY: ++healthy; break;
                    case INFECTED: ++infected; break;
                    case IMMUNE: ++immune; break;
                }
            }
            reportWriter.printf("%d %d %d%n", healthy, infected, immune);
        }
        reportWriter.close();
    }

    private boolean isProb(double d) {
        return (d >= 0.0 && d <= 1.0);
    }
    private void setNumOfAgents(int numOfAgents) {
        if (numOfAgents < 1 || numOfAgents > 1000000) throw new IllegalArgumentException();
        this.numOfAgents = numOfAgents;
    }

    private void setSociableChance(double sociableChance) {
        if (!isProb(sociableChance)) throw new IllegalArgumentException();
        this.sociableChance = sociableChance;
    }

    private void setMeetingChance(double meetingChance) {
        if (!isProb(meetingChance)) throw new IllegalArgumentException();
        this.meetingChance = meetingChance;
    }

    private void setInfectionChance(double infectionChance) {
        if (!isProb(infectionChance)) throw new IllegalArgumentException();
        this.infectionChance = infectionChance;
    }

    private void setCureChance(double cureChance) {
        if (!isProb(cureChance)) throw new IllegalArgumentException();
        this.cureChance = cureChance;
    }

    private void setDeathChance(double deathChance) {
        if (!isProb(deathChance)) throw new IllegalArgumentException();
        this.deathChance = deathChance;
    }

    private void setSimulationLength(int simulationLength) {
        if (simulationLength <= 0 || simulationLength > 1000) throw new IllegalArgumentException();
        this.simulationLength = simulationLength;
    }

    private void setAverageNeighbours(int averageNeighbours) {
        if (averageNeighbours < 0 || averageNeighbours > numOfAgents - 1) throw new IllegalArgumentException();
        this.averageNeighbours = averageNeighbours;
    }

    private void setSeed(long seed) {
        this.seed = seed;
    }

    private void setReportWriter(PrintWriter reportWriter) {
        this.reportWriter = reportWriter;
    }
}
