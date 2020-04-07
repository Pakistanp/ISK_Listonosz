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
        //Graph graphTest = new Graph(PATH + "graphTEST");
        Graph graphTest = new Graph(PATH + "graph1");

        Testy testy = new Testy(graphTest);
        Engine<BitGene, Integer> engine =
                Engine.builder(testy, BitChromosome.of((graphTest.amountOfVertex() - 1) * AMOUNT_OF_BITES_IN_BYTE,0.5))
                        .populationSize(100)
                        .optimize(Optimize.MINIMUM)
                        .survivorsSelector(new TournamentSelector<>(5))
                        .offspringSelector(new RouletteWheelSelector<>())
                        .alterers(new Mutator<>(0.115), new SinglePointCrossover<>(0.16))
                        .build();

        EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();
        Phenotype<BitGene, Integer> finalPhenotype = engine.stream()
                .limit(100)
                .peek(statistics)
                .collect(toBestPhenotype());

        System.out.println(statistics);
        //System.out.println(finalPhenotype);

        Result result = new Result(finalPhenotype, graphTest);
        System.out.println(result);
    }
}
