package ISK_Listonosz;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;

import java.util.List;
import java.util.function.Function;

public class Testy implements Function<Genotype<BitGene>, Integer> {
    private final Graph graph;
    private int check_again = 1;

    public Testy(Graph graph) {
        this.graph = graph;
    }


    @Override
    public Integer apply(Genotype<BitGene> chromosomes) {
        BitChromosome chromosome = chromosomes.getChromosome().as(BitChromosome.class);
        MetGraph chromosomeGraph = new MetGraph(graph, chromosome);
        //List<MetVertex> chromosomeVertex = chromosomeGraph.GetListOfMetVertex();
        int amountOfUsedCost = chromosomeGraph.amountOfUsedCost();
        boolean everyEdgesHasBeenMet = chromosomeGraph.everyEdgesHasBeenMet();
        boolean foundBackWay = chromosomeGraph.foundBackWay();
        if (everyEdgesHasBeenMet && foundBackWay) {
            return amountOfUsedCost;
        }
            //return graph.MaxCosts() - amountOfUsedCost;
        else
            return (graph.sumOfAll() + amountOfUsedCost);
    }
}
