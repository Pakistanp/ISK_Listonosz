package ISK_Listonosz;

import java.util.Map;
import java.util.Objects;

public class MetVertex implements AnyVertex {
    private final AnyVertex vertex;
    private final int chosenCost;

    public MetVertex(AnyVertex vertex, int chosenCost) {
        this.vertex = vertex;
        this.chosenCost = chosenCost;
    }

    public int GetChosenCost() {
        return this.chosenCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetVertex metVertex = (MetVertex) o;
        return chosenCost == metVertex.chosenCost &&
                Objects.equals(vertex, metVertex.vertex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertex, chosenCost);
    }

    @Override
    public int current() {
        return vertex.current();
    }

    @Override
    public Map<Integer, Integer> connectedVertexCosts() {
        return vertex.connectedVertexCosts();
    }

    @Override
    public int getNumberChosenVertexByCost(int chosenCost) {
        int number = 0;
        for (Map.Entry<Integer, Integer> vertexEntry : vertex.connectedVertexCosts().entrySet()) {
            if(vertexEntry.getValue() == chosenCost)
                number = vertexEntry.getKey();
                //break;
        }
        return number;
    }

}
