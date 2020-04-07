package ISK_Listonosz;

import java.util.Map;

public interface AnyVertex {
    int current();
    Map<Integer,Integer> connectedVertexCosts();

    int getNumberChosenVertexByCost(int chosenCost);
}
