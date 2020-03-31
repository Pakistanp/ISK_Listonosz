package ISK_Listonosz;

import java.io.IOException;
import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionStatistics;

import static io.jenetics.engine.EvolutionResult.toBestPhenotype;

public class RouteInspection {
    private final static int AMOUNT_OF_BITES_IN_BYTE = 8;

    private final static String PATH = "src/main/resources/graphs/";
    public static void main(String [] args) throws IOException {
        System.out.println("TEST");
        Graph graphOne = new Graph(PATH + "graphTEST");

        //System.out.println(statistics);
        //System.out.println(finalPhenotype);

    }
}
