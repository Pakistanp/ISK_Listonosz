package ISK_Listonosz;

import io.jenetics.BitChromosome;

import java.util.*;
import java.util.stream.IntStream;

public class MetGraph {
    private final List<MetVertex> metVertices;
    private final Graph graph;
    private List<Integer> graphData;
    private List<Integer> usedGraphData = new ArrayList<>();
    private List<AnyEdge> metEdges = new ArrayList<>();
    private int amountOfUsedDataToMakeAdditionalEgdes = 0;
    private int addedEdges = 0;
    private Graph additionalGraph;
    StringBuilder sb = new StringBuilder();

    public MetGraph(List<MetVertex> metVertices, Graph graph){
        this.metVertices = metVertices;
        this.graph = graph;
    }

    public MetGraph(Graph graph, BitChromosome chromosome) {
        this.graph = graph;
        amountOfUsedDataToMakeAdditionalEgdes = 0;
        metVertices = new ArrayList<>();
        List<AnyVertex> metVertexList = new ArrayList<>();
        List<AnyEdge> metEdgeList = new ArrayList<>();
        for (AnyVertex vertex : graph.vertices()) {
            metVertexList.add(new Vertex(vertex));
        }
        for (AnyEdge edge : graph.edges()) {
            metEdgeList.add(new Edge(edge));
        }
        additionalGraph = new Graph(metEdgeList, metVertexList);
        graphData = getListOfGraphIntDataFrom(chromosome);
        IntStream.range(0, graphData.size()).forEach(element -> graphData.set(element, graphData.get(element) + 128));
        try {
            sb.append("chooseStartVertex - ");
            chooseStartVertex(graphData.get(0));
            if (graph.amountOfUnpairedVertex() > 0) {
                sb.append("makeAdditionalEdges - ");
            }
            sb.append("chooseWays - ");
            chooseWays(graphData, additionalGraph.vertices());

            int cost = 0;
            for (int i = 0; i < additionalGraph.amountOfEdges(); i++) {
                if(usedGraphData.get(i) != -1) {
                    MetVertex newMetVertex;
                    sb.append("usedGraphData.get(i = " + i + ") - ");
                    AnyVertex vertex = additionalGraph.vertices().get(usedGraphData.get(i));
                    sb.append("cost - ");
                    cost = vertex.connectedVertexCosts().get(graphData.get(i + 1 + amountOfUsedDataToMakeAdditionalEgdes));
                    sb.append("All DONE - ");
                    newMetVertex = new MetVertex(vertex, cost);

                    metVertices.add(newMetVertex);
                }
            }

        } catch (Exception e) {
            System.out.println(e + "\n" +sb.toString());
        }
    }

    private void makeAdditionalEdges2( List<Integer> graphData, List<AnyVertex> vertices) {
        List<Integer> addedEgdesList = new ArrayList<>();

        List<AnyVertex> unpairedList = new ArrayList<>();
        for (AnyVertex vertex : vertices){
            if (vertex.connectedVertexCosts().size() % 2 != 0) {
                unpairedList.add(vertex);
            }
        }
        for (int unpairVertex = 0 ; unpairVertex < unpairedList.size(); unpairVertex ++) {

            if( addedEgdesList.contains(unpairedList.get(unpairVertex).current()) ) {
                continue;
            }

            for (int i = 0 ; i < unpairedList.size(); i ++) {
                for (int k = 0; k < addedEgdesList.size(); k++){
                    if (unpairedList.get(i).current() == addedEgdesList.get(k)){
                        unpairedList.remove(i);
                    }
                }
            }

            boolean added = false;
            List<AnyVertex> metVertexList = new ArrayList<>();
            for (AnyVertex vertex : vertices) {
                metVertexList.add(new Vertex(vertex));
            }
            for (int j = 0; j < unpairedList.size(); j++) {
                if (unpairVertex != j && unpairedList.get(unpairVertex).connectedVertexCosts().containsKey(unpairedList.get(j).current())
                        && !addedEgdesList.contains(unpairedList.get(unpairVertex).current()) && !addedEgdesList.contains(unpairedList.get(j).current())) {
                    AnyEdge edge = additionalGraph.getEdgeByVertex(unpairedList.get(unpairVertex).current(), unpairedList.get(j).current());
                    additionalGraph.edges().add(new Edge(edge.firstVertex(), edge.secondVertex(), edge.cost(), edge.multipleUse() + 1));
                    addedEgdesList.add(edge.firstVertex());
                    addedEgdesList.add(edge.secondVertex());

                    Map<Integer,Integer> connectedVertexCosts = additionalGraph.vertices().get(edge.firstVertex() - 1).connectedVertexCosts();
                    connectedVertexCosts.put(edge.secondVertex(), edge.cost());
                    connectedVertexCosts = additionalGraph.vertices().get(edge.secondVertex() - 1).connectedVertexCosts();
                    connectedVertexCosts.put(edge.firstVertex(), edge.cost());
                    added = true;
                    break;
                }

            }

            if(!added) {
                int tempAmountOfUsedDataToMakeAdditionalEgdes = 0;

                int startVertex = unpairedList.get(unpairVertex).current();
                int prevVertex = startVertex;
                int nextVertex = startVertex;
                int allCount = 0;
                added = false;
                for (int k = 1; k <= graph.amountOfEdges(); k++) {
                    int section = (int) Math.ceil((float) 256 / metVertexList.get(nextVertex - 1).connectedVertexCosts().size());
                    int i = 1;
                    for (Map.Entry<Integer, Integer> vertexEntry : metVertexList.get(nextVertex - 1).connectedVertexCosts().entrySet()) {
                        if ((section * i + 1) >= graphData.get(k) && !added) {
                            graphData.set(k, vertexEntry.getKey());
                            tempAmountOfUsedDataToMakeAdditionalEgdes += 1;
                            prevVertex = nextVertex;
                            nextVertex = vertexEntry.getKey();
                            allCount += vertexEntry.getValue();
                            for (AnyVertex ver : unpairedList) {
                                if (!ver.equals(unpairedList.get(unpairVertex))) {
                                    if (ver.current() == nextVertex && !addedEgdesList.contains(nextVertex)) {
                                        Map<Integer, Integer> connectedVertexCosts = additionalGraph.vertices().get(startVertex - 1).connectedVertexCosts();
                                        additionalGraph.edges().add(new Edge(startVertex, nextVertex, allCount, 1));

                                        amountOfUsedDataToMakeAdditionalEgdes += tempAmountOfUsedDataToMakeAdditionalEgdes;

                                        addedEgdesList.add(startVertex);
                                        addedEgdesList.add(nextVertex);

                                        connectedVertexCosts.put(nextVertex, allCount);
                                        connectedVertexCosts = additionalGraph.vertices().get(nextVertex - 1).connectedVertexCosts();
                                        connectedVertexCosts.put(startVertex, allCount);

                                        added = true;
                                        break;
                                    }
                                }
                            }
                            metVertexList.get(nextVertex - 1).connectedVertexCosts().remove(prevVertex);
                            break;
                        }
                        i++;
                    }
                    if (added)
                        break;
                }
            }

        }

    }
    private void makeAdditionalEdges(List<Integer> graphData, List<AnyVertex> vertices) {
        boolean isTargetFound = false;
        int tempAmountOfUsedDataToMakeAdditionalEgdes = 0;
        List<Integer> addedEgdesList = new ArrayList<>();

        List<AnyVertex> unpairedList = new ArrayList<>();
        for (AnyVertex vertex : vertices){
            if (vertex.connectedVertexCosts().size() % 2 != 0) {
                unpairedList.add(vertex);
            }
        }

        for (int unpairVertex = 0 ; unpairVertex < unpairedList.size(); unpairVertex ++) {
            if( addedEgdesList.contains(unpairedList.get(unpairVertex).current()) ) {
                continue;
            }
            List<AnyVertex> metVertexList = new ArrayList<>();
            for (AnyVertex vertex : vertices) {
                metVertexList.add(new Vertex(vertex));
            }
            tempAmountOfUsedDataToMakeAdditionalEgdes = 0;

            int startVertex = unpairedList.get(unpairVertex).current();
            int prevVertex = startVertex;
            int nextVertex = startVertex;
            int allCount = 0;
            boolean added = false;
            for (int k = 1; k <= graph.amountOfEdges(); k++) {
                int section = (int) Math.ceil((float) 256 / metVertexList.get(nextVertex - 1).connectedVertexCosts().size());
                int i = 1;
                for (Map.Entry<Integer, Integer> vertexEntry : metVertexList.get(nextVertex - 1).connectedVertexCosts().entrySet()) {
                    if ((section * i + 1) >= graphData.get(k) && !added) {
                        graphData.set(k, vertexEntry.getKey());
                        tempAmountOfUsedDataToMakeAdditionalEgdes += 1;
                        prevVertex = nextVertex;
                        nextVertex = vertexEntry.getKey();
                        allCount += vertexEntry.getValue();
                        for (AnyVertex ver : unpairedList) {
                            if (!ver.equals(unpairedList.get(unpairVertex))) {
                                if (ver.current() == nextVertex && !addedEgdesList.contains(nextVertex)) {
                                    Map<Integer,Integer> connectedVertexCosts = additionalGraph.vertices().get(startVertex - 1).connectedVertexCosts();
                                    additionalGraph.edges().add(new Edge(startVertex, nextVertex, allCount, 1));

                                    amountOfUsedDataToMakeAdditionalEgdes += tempAmountOfUsedDataToMakeAdditionalEgdes;

                                    addedEgdesList.add(startVertex);
                                    addedEgdesList.add(nextVertex);

                                    connectedVertexCosts.put(nextVertex, allCount);
                                    connectedVertexCosts = additionalGraph.vertices().get(nextVertex - 1).connectedVertexCosts();
                                    connectedVertexCosts.put(startVertex, allCount);

                                    added = true;
                                    break;
                                }
                            }
                        }
                        metVertexList.get(nextVertex - 1).connectedVertexCosts().remove(prevVertex);
                        break;
                    }
                    i++;
                }
                if (added)
                    break;
            }
        }
    }

    private List<Integer> getListOfGraphIntDataFrom(BitChromosome chromosome) {
        byte[] graphDataInBytes = chromosome.toByteArray();
        Integer[] graphDataInInts = new Integer[graphDataInBytes.length];
        for (int i = 0; i < graphDataInInts.length; i++) {
            graphDataInInts[i] = (int)graphDataInBytes[i];
        }
        return new ArrayList<>(Arrays.asList(graphDataInInts));
    }

    public List<MetVertex> GetListOfMetVertex() {
        return this.metVertices;
    }

    public int amountOfUsedCost() {
        int sum = 0;
        for (AnyEdge metEgde : metEdges)
            sum += metEgde.cost();
        return sum;
    }

    public boolean everyEdgesHasBeenMet() {
        return containsAll(metEdges, graph.edges());
    }

    public boolean everyEdgesHasBeenMet2() {
        List<Edge> tempEdgesList = new ArrayList<>();
        boolean res = true;
        for (int i = 0; i < metEdges.size(); i++) {
            if (!tempEdgesList.contains(metEdges.get(i)) && metEdges.get(i).multipleUse() > 0) {
                ((Edge)metEdges.get(i)).setMultipleUse(metEdges.get(i).multipleUse() - 1);
                tempEdgesList.add((Edge) metEdges.get(i));
            }
            else {
                res = false;
                break;
            }
        }
        return res;
    }

    public boolean foundBackWay() {
        return metEdges.get(0).firstVertex() == metEdges.get(metEdges.size() - 1).secondVertex();
    }

    private void chooseStartVertex (int startValue) {
        int section = (int) Math.ceil((float)255 / graph.amountOfVertex());
        for (int i = 1; i <= graph.amountOfVertex(); i++) {
            if ((section * i + 1) >= startValue) {
                graphData.set(0, i);
                break;
            }
        }
    }
    public boolean isCorrectWay() {
        int timesMet = 0;
        for (AnyVertex vertex : graph.vertices()) {
            //vertex.
        }
        return true;
    }

    private void chooseWays(List<Integer> graphData, List<AnyVertex> vertices) {
        int startVertex = graphData.get(0);
        int prevVertex = startVertex;
        int nextVertex = startVertex;
        boolean isTargetFound = false;
        List<AnyVertex> metVertexList = new ArrayList<>();
        for (AnyVertex vertex : vertices) {
            metVertexList.add(new Vertex(vertex));
        }
        int k = 1 + amountOfUsedDataToMakeAdditionalEgdes;
        while(!containsAll(metEdges, graph.edges())) {
            int section = (int) Math.ceil((float)256 / metVertexList.get(nextVertex - 1).connectedVertexCosts().size());
            int i = 1;
            for (Map.Entry<Integer, Integer> vertexEntry : metVertexList.get(nextVertex - 1).connectedVertexCosts().entrySet()) {
                if((section * i + 1) >= graphData.get(k)) {
                        usedGraphData.add(nextVertex - 1);
                        graphData.set(k, vertexEntry.getKey());
                        prevVertex = nextVertex;
                        nextVertex = vertexEntry.getKey();
                        int multiple = additionalGraph.getEdgeByVertex(prevVertex, nextVertex).multipleUse();
                        Edge edge = new Edge(prevVertex, nextVertex, vertexEntry.getValue(), multiple);

                        metEdges.add(edge);

                        break;
                }
                i++;
            }
            k++;
            if (k >=((graph.amountOfEdges() * 2 + (graph.amountOfEdges() * graph.amountOfUnpairedVertex())) + 1)){
                break;
            }
        }
    }

    private boolean containsAll(List<AnyEdge> list1, List<AnyEdge> list2) {
        Iterator<?> e = list2.iterator();
        while (e.hasNext())
            if (!contains(list1, (AnyEdge) e.next()))
                return false;
        return true;
    }

    private boolean contains(List<AnyEdge> list, AnyEdge o){
        boolean res = false;
        for(int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(o)){
                res = true;
            }
        }
        return res;
    }

    private void findBackPath(List<Integer> graphData, List<AnyVertex> vertices) {
        int nextVertex = metVertices.get(metVertices.size()-1).getNumberChosenVertexByCost(metVertices.get(metVertices.size()-1).GetChosenCost());
        int prevVertex = 1;
        int target = vertices.get(0).current();
        boolean isTargetFound = false;
        List<AnyVertex> metVertexList = new ArrayList<>();
        for (AnyVertex vertex : vertices) {
            metVertexList.add(new Vertex(vertex));
        }
        for (int k = (graph.amountOfVertex() - 1); k < graphData.size(); k++) {
            int section = (int) Math.ceil(256 / metVertexList.get(nextVertex - 1).connectedVertexCosts().size());
            int i = 1;
            for (Map.Entry<Integer, Integer> vertexEntry : metVertexList.get(nextVertex - 1).connectedVertexCosts().entrySet()) {
                if (isTargetFound) {
                    usedGraphData.add(-1);
                    break;
                }
                else if ((section * i + 1) >= graphData.get(k)) {
                    usedGraphData.add(nextVertex - 1);
                    graphData.set(k, vertexEntry.getKey());
                    if (target != vertexEntry.getKey()) {
                        prevVertex = nextVertex;
                        nextVertex = vertexEntry.getKey();
                        metVertexList.get(nextVertex - 1).connectedVertexCosts().remove(prevVertex);
                        break;
                    }
                    else {
                        isTargetFound = true;
                        break;
                    }
                }
                i++;
            }
        }
    }
    //private void removeMetFromAllVertex()

    public boolean compareEdgeList(List<Edge> edgesList, Edge compareEdge) {
        boolean res = false;
        for (Edge edge : edgesList) {
           if(edge.firstVertex() == compareEdge.firstVertex() && edge.secondVertex() == compareEdge.secondVertex()) {
               res = true;
               break;
           }
        }
        return res;
    }
    public String stringEdges(){
        StringBuilder sb = new StringBuilder();
        for (AnyEdge edge: additionalGraph.edges()) {
            sb.append(edge.toString() + "\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder graphAsString = new StringBuilder();
        metEdges.forEach(egde -> {
            graphAsString.append(egde.toString() + "\n");
        });
        //graphAsString.append(metVertices.get(metVertices.size()-1).getNumberChosenVertexByCost(metVertices.get(metVertices.size()-1).GetChosenCost()));
        return graphAsString.toString();
    }
    public List<Integer> GetGraphData() {
        return graphData;
    }
}
