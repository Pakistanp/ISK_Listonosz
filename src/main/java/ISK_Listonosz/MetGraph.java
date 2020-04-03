package ISK_Listonosz;

import io.jenetics.BitChromosome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class MetGraph {
    private final List<MetVertex> metVertices;
    private final Graph graph;

    public MetGraph(List<MetVertex> metVertices, Graph graph){
        this.metVertices = metVertices;
        this.graph = graph;
    }

    public MetGraph(Graph graph, BitChromosome chromosome) {
        this.graph = graph;
        List<Integer> graphData = getListOfGraphIntDataFrom(chromosome);
        chooseWays(graphData, graph.vertices());
        metVertices = new ArrayList<>();
        int cost = 0;
        for (int i = 0; i < graphData.size(); i++) {
            MetVertex newMetVertex;
            if (i == 0) {
                AnyVertex vertex = graph.vertices().get(i);
                cost = vertex.connectedVertexCosts().get(graphData.get(i));
                newMetVertex = new MetVertex(vertex,0);
            }
            else {
                newMetVertex = new MetVertex(graph.vertices().get(i),cost);
                cost = graph.vertices().get(i).connectedVertexCosts().get(graphData.get(i));
            }
            metVertices.add(newMetVertex);
        }
    }

    private List<Integer> getListOfGraphIntDataFrom(BitChromosome chromosome) {
        byte[] graphDataInBytes = chromosome.toByteArray();
        Integer[] graphDataInInts = new Integer[graphDataInBytes.length];
        for (int i = 0; i < graphDataInInts.length; i++) {
            graphDataInInts[i] = Math.abs(graphDataInBytes[i]);
        }
        return new ArrayList<>(Arrays.asList(graphDataInInts));
    }

    public List<MetVertex> GetListOfMetVertex() {
        return this.metVertices;
    }

    public int amountOfUsedCost() {
        int sum = 0;
        for (MetVertex metVertex : metVertices)
            sum += metVertex.GetChosenCost();
        return sum;
    }

    public boolean everyVertexHasBeenMet() {
        return graph.amountOfVertex() == metVertices.size();
    }

    private void chooseWays(List<Integer> graphData, List<AnyVertex> vertices) {
        for (int k = 0; k < graphData.size(); k++) {
            int section = (int) Math.ceil(127 / vertices.get(k).connectedVertexCosts().size());
            int i = 1;
            for (Map.Entry<Integer, Integer> vertexEntry : vertices.get(k).connectedVertexCosts().entrySet()) {
                if ((section * i + 1) >= graphData.get(k)) {
                    graphData.set(k, vertexEntry.getKey());
                    break;
                }
                i++;
            }
        }
    }
}
