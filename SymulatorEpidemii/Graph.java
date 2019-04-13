package SymulatorEpidemii;

import java.util.*;

public class Graph<T extends Vertex<T>> implements Iterable<T> {
    private ArrayList<T> vertices;

    public Graph(int v, int e, RandomFactory<T> factory) {
        vertices = new ArrayList<>(v);
        for (int i = 0; i < v; ++i) {
            vertices.add(factory.randomNew());
        }
        if (v > 40000 || e <= v * (v-1) / 16) {
            addRandomEdgesSparse(v, e);
        } else {
            addRandomEdges(v, e);
        }
    }

    private void addRandomEdgesSparse(int v, int e) {
        Set<Long> set = new LinkedHashSet<>();
        while (set.size() < e) {
            long a = Rng.nextInt(v);
            long b = Rng.nextInt(v);
            if (a == b) continue;
            set.add((Math.max(a,b) << 32) + Math.min(a,b));
        }
        for (long pair : set) {
            T a = vertices.get((int)pair);
            T b = vertices.get((int)(pair >> 32));
            a.addNeighbor(b);
            b.addNeighbor(a);
        }
    }

    private void addRandomEdges(int v, int e) {
        long[] permutation = new long[v*(v-1)/2];
        int index = 0;
        for (long i = 0; i < v - 1; ++i)
            for (long j = i+1; j < v; ++j)
                permutation[index++] = ((j << 32) + i);
        for (int i = 0; i < e; ++i) {
            int swap = i + Rng.nextInt(v*(v-1)/2-i);
            T a = vertices.get((int)permutation[swap]);
            T b = vertices.get((int)(permutation[swap] >> 32));
            a.addNeighbor(b);
            b.addNeighbor(a);
            permutation[swap] = permutation[i];
        }
    }

    public T getRandomVertex() {
        return vertices.get(Rng.nextInt(vertices.size()));
    }

    @Override
    public Iterator<T> iterator() {
        return vertices.iterator();
    }

}
