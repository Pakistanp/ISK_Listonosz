package ISK_Listonosz;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Graph implements AnyGraph{
    private final int CORRECT_AMOUNT_OF_EDGE_DATA_IN_ONE_LINE = 3;

    private List<AnyEdge> edges;

    public Graph(String filePath) {
        Path graphFilePath = Paths.get(filePath);
        try {
            Files.lines(graphFilePath).forEach(this::LoadEdgesAndCosts);
        } catch (IOException e) {
            throw new IllegalStateException("An error when initialising the graph");
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
            //edges.add(new Edge(firstVertex, secondVertex, cost));
        }
    }

    public List<AnyEdge> edges() {
        return new ArrayList<>(edges);
    }

    @Override
    public int amountOfVertex() {
        return 0;
    }
}
