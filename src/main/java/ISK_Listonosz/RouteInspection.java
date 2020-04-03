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

        Testy testy = new Testy(graphOne);
        Engine<BitGene, Integer> engine =
                Engine.builder(testy, BitChromosome.of(graphOne.amountOfVertex() * AMOUNT_OF_BITES_IN_BYTE,0.5))
                .populationSize(100)
                .survivorsSelector(new TournamentSelector<>(5))
                .offspringSelector(new RouletteWheelSelector<>())
                .alterers(new Mutator<>(0.115), new SinglePointCrossover<>(0.16))
                .build();

        EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();
//        Phenotype<BitGene, Integer> finalPhenotype = engine.stream()
//                .limit(100)
//                .peek(statistics)
//                .collect(toBestPhenotype());

        System.out.println(statistics);
//        System.out.println(finalPhenotype);

    }
}
