package ISK_Listonosz;

import java.util.List;

public interface AnyGraph {
    List<AnyEdge> edges();
    List<AnyVertex> vertices();

    int sumOfAll();
    int amountOfVertex();
    int amountOfEdges();
}
