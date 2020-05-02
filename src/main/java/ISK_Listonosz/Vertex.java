package ISK_Listonosz;

import java.util.*;

public class Vertex implements AnyVertex {
    private final int currentVertex;
    private final Map<Integer,Integer> connectedVertexCosts;

    public Vertex(int currentVertex, Map<Integer, Integer> connectedVertexCosts) {
        this.currentVertex = currentVertex;
        this.connectedVertexCosts = connectedVertexCosts;
    }

    public Vertex(AnyVertex vertex) {
        this.currentVertex = vertex.current();
        this.connectedVertexCosts = new HashMap<>();
        this.connectedVertexCosts.putAll(vertex.connectedVertexCosts());
    }

    @Override
    public int getNumberChosenVertexByCost(int chosenCost) {
        int number = 0;
        for (Map.Entry<Integer, Integer> vertexEntry : connectedVertexCosts.entrySet()) {
            if(vertexEntry.getValue() == chosenCost)
                number = vertexEntry.getKey();
        }
        return number;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return currentVertex == vertex.currentVertex &&
                Objects.equals(connectedVertexCosts, vertex.connectedVertexCosts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentVertex, connectedVertexCosts);
    }

    @Override
    public int current() {
        return currentVertex;
    }

    @Override
    public Map<Integer, Integer> connectedVertexCosts() {
        return connectedVertexCosts;
    }
}
