package ISK_Listonosz;

import java.util.Objects;

public class Edge implements AnyEdge {
    private final int firstVertex;
    private final int secondVertex;
    private final int cost;
    private int multipleUse;

    public Edge(int firstVertex, int secondVertex, int cost, int multipleUse) {
        this.firstVertex = firstVertex;
        this.secondVertex = secondVertex;
        this.cost = cost;
        this.multipleUse = multipleUse;
    }

    public Edge(AnyEdge edge) {
        this.firstVertex = edge.firstVertex();
        this.secondVertex = edge.secondVertex();
        this.cost = edge.cost();
        this.multipleUse = edge.multipleUse();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return ((firstVertex == edge.firstVertex &&
                secondVertex == edge.secondVertex) ||
                (firstVertex == edge.secondVertex &&
                secondVertex == edge.firstVertex)) &&
                cost == edge.cost &&
                multipleUse == edge.multipleUse;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstVertex, secondVertex, cost, multipleUse);
    }

    @Override
    public String toString() {
        return "("
                + firstVertex +
                "-" + secondVertex +
                ":" + cost +
                "{" + multipleUse + "}" +
                ')';
    }

    public void setMultipleUse(int value) {
        multipleUse = value;
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
    public int multipleUse() {
        return multipleUse;
    }

    @Override
    public int cost() {
        return cost;
    }
}
