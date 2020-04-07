package ISK_Listonosz;

import io.jenetics.BitChromosome;

import java.util.*;
import java.util.stream.IntStream;

public class MetGraph {
    private final List<MetVertex> metVertices;
    private final Graph graph;
    private List<Integer> graphData;
    private List<Integer> usedGraphData = new ArrayList<>();

    public MetGraph(List<MetVertex> metVertices, Graph graph){
        this.metVertices = metVertices;
        this.graph = graph;
    }

    public MetGraph(Graph graph, BitChromosome chromosome) {
        this.graph = graph;
        metVertices = new ArrayList<>();
        graphData = getListOfGraphIntDataFrom(chromosome);
        IntStream.range(0, graphData.size()).forEach(element -> graphData.set(element, graphData.get(element) + 128));
        try {
            chooseWays(graphData, graph.vertices());
            int cost = 0;
            for (int i = 0; i < graphData.size(); i++) {
                MetVertex newMetVertex;
                AnyVertex vertex = graph.vertices().get(usedGraphData.get(i));
                cost = vertex.connectedVertexCosts().get(graphData.get(i));
                newMetVertex = new MetVertex(vertex, cost);

                metVertices.add(newMetVertex);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private List<Integer> getListOfGraphIntDataFrom(BitChromosome chromosome) {
        byte[] graphDataInBytes = chromosome.toByteArray();
        Integer[] graphDataInInts = new Integer[graphDataInBytes.length];
        for (int i = 0; i < graphDataInInts.length; i++) {
            graphDataInInts[i] = (int)graphDataInBytes[i];
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
        List<Integer> tempVertexList = new ArrayList<>();
        tempVertexList.add(1);
        boolean res = true;
        for (int i = 0; i < graphData.size(); i++) {
            if (!tempVertexList.contains(graphData.get(i)))
                tempVertexList.add(graphData.get(i));
            else {
                res = false;
                break;
            }
        }
        return res;
    }

    public boolean isCorrectWay() {
        int timesMet = 0;
        for (AnyVertex vertex : graph.vertices()) {
            //vertex.
        }
        return true;
    }

//    private void chooseWays(List<Integer> graphData, List<AnyVertex> vertices) {
//        for (int k = 0; k < graphData.size(); k++) {
//            int section = (int) Math.ceil(256 / vertices.get(k).connectedVertexCosts().size());
//            int i = 1;
//            for (Map.Entry<Integer, Integer> vertexEntry : vertices.get(k).connectedVertexCosts().entrySet()) {
//                if ((section * i + 1) >= graphData.get(k)) {
//                    graphData.set(k, vertexEntry.getKey());
//                    break;
//                }
//                i++;
//            }
//        }
//    }
    private void chooseWays(List<Integer> graphData, List<AnyVertex> vertices) {
        int nextVertex = 1;
        int prevVertex = 1;
        List<AnyVertex> metVertexList = new ArrayList<>();
        for (AnyVertex vertex : vertices) {
            metVertexList.add(new Vertex(vertex));
        }
        for (int k = 0; k < graphData.size(); k++) {
            int section = (int) Math.ceil(256 / metVertexList.get(nextVertex - 1).connectedVertexCosts().size());
            int i = 1;
            for (Map.Entry<Integer, Integer> vertexEntry : metVertexList.get(nextVertex - 1).connectedVertexCosts().entrySet()) {
                if ((section * i + 1) >= graphData.get(k)) {
                    usedGraphData.add(nextVertex - 1);
                    graphData.set(k, vertexEntry.getKey());
                    prevVertex = nextVertex;
                    nextVertex = vertexEntry.getKey();
                    metVertexList.get(nextVertex - 1).connectedVertexCosts().remove(prevVertex);
                    break;
                }
                i++;
            }
        }
    }

    //private void removeMetFromAllVertex()

    @Override
    public String toString() {
        StringBuilder graphAsString = new StringBuilder();
        metVertices.forEach(vertex -> {
            graphAsString.append(vertex.current()).append("-").append("(").append(vertex.GetChosenCost()).append(")").append("-");
        });
        graphAsString.append(metVertices.get(metVertices.size()-1).getNumberChosenVertexByCost(metVertices.get(metVertices.size()-1).GetChosenCost()));
        return graphAsString.toString();
    }
    public List<Integer> GetGraphData() {
        return graphData;
    }
}
