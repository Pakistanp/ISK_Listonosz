package ISK_Listonosz;

import java.util.Objects;

public class Edge implements AnyEdge {
    private final int firstVertex;
    private final int secondVertex;
    private final int cost;

    public Edge(int firstVertex, int secondVertex, int cost) {
        this.firstVertex = firstVertex;
        this.secondVertex = secondVertex;
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return firstVertex == edge.firstVertex &&
                secondVertex == edge.secondVertex &&
                cost == edge.cost;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstVertex, secondVertex, cost);
    }

    @Override
    public String toString() {
        return "("
                + firstVertex +
                "-" + secondVertex +
                ":" + cost +
                ')';
    }

    @Override
    public int firstVertex() {
        return firstVertex;
    }

    @Override
    public int secondVertex() {
        return secondVertex;
    }

    @Override
    public int cost() {
        return cost;
    }
}
