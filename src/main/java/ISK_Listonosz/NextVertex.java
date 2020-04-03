package ISK_Listonosz;

import java.util.ArrayList;
import java.util.Map;

public class NextVertex {
    private final ArrayList<Integer> previousVertexes;
    private final AnyVertex currentVertex;

    public NextVertex(ArrayList<Integer> previousVertexes, AnyVertex currentVertex) {
        this.previousVertexes = previousVertexes;
        this.currentVertex = currentVertex;
    }

    public void MoveToNext(int nextVertex) {
        previousVertexes.add(nextVertex);
    }

    public int GetBestWay() {
        int minCost = -1;
        int bestWay = 0;
        for (Map.Entry<Integer, Integer> vertexCost:currentVertex.connectedVertexCosts().entrySet()) {
            if (previousVertexes.contains(vertexCost.getKey()))
                continue;

            if (minCost == -1) {
                minCost = vertexCost.getValue();
                bestWay = vertexCost.getKey();
            }
            else if (minCost > vertexCost.getValue()) {
                minCost = vertexCost.getValue();
                bestWay = vertexCost.getKey();
            }
        }
        return bestWay;
    }
}
