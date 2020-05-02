package ISK_Listonosz;

import io.jenetics.BitChromosome;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

public class Graph implements AnyGraph{
    private final int CORRECT_AMOUNT_OF_EDGE_DATA_IN_ONE_LINE = 3;

    private List<AnyEdge> edges;
    private List<AnyVertex> vertices;
    private int countOfVertex;
    private int sumOfAll;
    private int maxCost;
    private int amountOfUnpairedVertex = 0;

    public Graph(List<AnyEdge> edges, List<AnyVertex> vertices){
        this.edges = edges;
        this.vertices = vertices;
    }

    public Graph(String filePath) {
        Path graphFilePath = Paths.get(filePath);
        try {
            edges = new ArrayList<>();
            vertices = new ArrayList<>();
            Files.lines(graphFilePath).forEach(this::LoadEdgesAndCosts);
            LoadVertexes();
            MaxCost();
        } catch (IOException e) {
            throw new IllegalStateException("An error when initialising the graph");
        }
    }

    private void LoadVertexes(){
        for (int i = 1; i <= countOfVertex; i++) {
            Map<Integer,Integer> connectedVertexCosts = new HashMap<>();
            for (AnyEdge edge : edges) {
                if (edge.firstVertex() == i) {
                    connectedVertexCosts.put(edge.secondVertex(), edge.cost());
                }
                else if (edge.secondVertex() == i) {
                    connectedVertexCosts.put(edge.firstVertex(), edge.cost());
                }
            }
            vertices.add(new Vertex(i, connectedVertexCosts));
        }
    }

    private void LoadEdgesAndCosts(String line) {
        String[] splitLine = line.split(" ");
        int amountOfEdgeData = splitLine.length;
        if (amountOfEdgeData != CORRECT_AMOUNT_OF_EDGE_DATA_IN_ONE_LINE) {
            throw new IllegalArgumentException("Graph file is incorrect, possibly corrupted. \nError line: " + line);
        } else {
            int firstVertex = Integer.parseInt(splitLine[0]);
            int secondVertex = Integer.parseInt(splitLine[1]);
            int cost = Integer.parseInt(splitLine[2]);
            sumOfAll += cost;
            edges.add(new Edge(firstVertex, secondVertex, cost, 1));

            if (firstVertex > countOfVertex)
                countOfVertex = firstVertex;
            else if (secondVertex > countOfVertex)
                countOfVertex = secondVertex;
        }
    }

    public void MaxCost() {
        int maxCost = 0;
        for (AnyEdge edge : edges) {
            maxCost += edge.cost();
        }
        this.maxCost = maxCost;
    }


    public boolean HaveUnpairedVertex() {
        boolean res = false;
        for (AnyVertex vertex : vertices) {
            if (vertex.connectedVertexCosts().size() % 2 != 0) {
                amountOfUnpairedVertex += 1;
                res = true;
            }
        }
        return res;
    }

    public int amountOfUnpairedVertex() {
        return amountOfUnpairedVertex;
    }

    public List<AnyEdge> edges() {
        return edges;
    }

    public int MaxCosts() {
        return this.maxCost;
    }

    @Override
    public List<AnyVertex> vertices() {
        return vertices;
    }

    @Override
    public int sumOfAll() {
        return sumOfAll;
    }

    @Override
    public int amountOfVertex() {
        return countOfVertex;
    }

    @Override
    public int amountOfEdges() {
        return edges.size();
    }

    public AnyEdge getEdgeByVertex(int firstVertex, int secondVertex) {
        for (AnyEdge edge : edges ) {
            if ((edge.firstVertex() == firstVertex && edge.secondVertex() == secondVertex) || (edge.firstVertex() == secondVertex && edge.secondVertex() == firstVertex) ) {
                return edge;
            }
        }
        return null;
    }
}
