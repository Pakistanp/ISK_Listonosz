package ISK_Listonosz;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionStatistics;

import static io.jenetics.engine.EvolutionResult.toBestPhenotype;

public class RouteInspection {
    private final static int AMOUNT_OF_BITES_IN_BYTE = 8;

    private final static String PATH = "src/main/resources/graphs/";
    public static void main(String [] args) throws IOException {
        //Graph graphTest = new Graph(PATH + "graphTEST2");
        Graph graphTest = new Graph(PATH + "graph2");

        Testy testy = new Testy(graphTest);

        int bits = 0;
        if (graphTest.HaveUnpairedVertex()) {
            bits = ((graphTest.amountOfEdges() * 2 + (graphTest.amountOfEdges() * graphTest.amountOfUnpairedVertex())) + 1) * AMOUNT_OF_BITES_IN_BYTE;
        }
        else {
            bits = (graphTest.amountOfEdges() * 2 + 1) * AMOUNT_OF_BITES_IN_BYTE;
        }

        FileWriter fw = new FileWriter("resultsGraph2Limit.txt");

        for (int populationLimit = 1; populationLimit <= 10000; populationLimit++) {
            long start = System.currentTimeMillis();

            Engine<BitGene, Integer> engine =
                    Engine.builder(testy, BitChromosome.of(bits, 0.5))
                            .populationSize(100)
                            .optimize(Optimize.MINIMUM)
                            .survivorsSelector(new TournamentSelector<>(5))
                            .offspringSelector(new RouletteWheelSelector<>())
                            .alterers(new Mutator<>(0.115), new SinglePointCrossover<>(0.16))
                            .build();

            EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();
            Phenotype<BitGene, Integer> finalPhenotype = engine.stream()
                    .limit(populationLimit)
                    .peek(statistics)
                    .collect(toBestPhenotype());

            long end = System.currentTimeMillis();
            long interval = end - start;
            //System.out.println(interval.getNano() / 1000000000.0);
            Result result = new Result(finalPhenotype, graphTest);

            fw.write(populationLimit + " " + result.getAmountOfUsedCost() + " " + interval / 1000.0 + "\n");
            System.out.println(populationLimit);
        }
        fw.close();
        //System.out.println(statistics);
        //System.out.println(finalPhenotype);

        //Result result = new Result(finalPhenotype, graphTest);
        //System.out.println(result);
    }
}
