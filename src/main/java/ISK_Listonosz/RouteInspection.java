package ISK_Listonosz;

import java.io.IOException;

public class RouteInspection {

    private final static String PATH = "src/main/resources/graphs/";
    public static void main(String [] args) throws IOException {
        System.out.println("TEST");
        Graph graphOne = new Graph(PATH + "graphTEST");
    }
}
