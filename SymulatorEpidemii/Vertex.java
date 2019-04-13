package SymulatorEpidemii;

import java.util.ArrayList;

public interface Vertex<T> {
    ArrayList<T> getNeighbors();
    void removeNeighbor(T v);
    void addNeighbor(T v);
    default T getRandomNeighbor() {
        return getNeighbors().get(Rng.nextInt(getNeighbors().size()));
    }
}
