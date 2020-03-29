package ISK_Listonosz;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Graph {

    public Graph(String filePath) throws IOException {
        Path graphFilePath = Paths.get(filePath);
        try {
            Files.lines(graphFilePath).forEach(this::LoadEdgesAndCosts);
        } catch (IOException e) {
            throw new IllegalStateException("An error when initialising the graph");
        }

    }

    private void LoadEdgesAndCosts(String line) {

    }
}
