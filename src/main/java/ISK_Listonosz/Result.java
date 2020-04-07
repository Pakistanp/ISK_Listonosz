package ISK_Listonosz;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Phenotype;

public class Result {
    private final MetGraph resultGraph;
    private final Phenotype<BitGene, Integer> finalPhenotype;

    public Result(Phenotype<BitGene, Integer> finalPhenotype, Graph testedGraph) {
        resultGraph = new MetGraph(testedGraph , finalPhenotype.getGenotype().getChromosome().as(BitChromosome.class));
        this.finalPhenotype = finalPhenotype;
    }

    @Override
    public String toString() {
        return finalPhenotype.toString() + "\n" +
                "Best way:\n" + resultGraph +
                "\nAmount of used cost is:\n" + resultGraph.amountOfUsedCost();
    }
}
