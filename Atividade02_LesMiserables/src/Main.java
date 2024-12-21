import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lib.BreadthFirstPaths;
import lib.Graph;
import lib.StdOut;

public class Main {

    public static void main(String[] args) throws Exception {
        Graph graph = readGraphFromGexf();

        System.out.println("Les Miserables Graph: ");
        StdOut.println(graph);


        System.out.println("Betweenness Centrality: ");
        double[] bcs = calculateBetweennessCentrality(graph);
        for (int i = 0; i < bcs.length; i++) {
            StdOut.println("Betweenness Centrality do vértice " + i + ": " + bcs[i]);
        }
        System.out.println();
    }

    private static Graph readGraphFromGexf() throws Exception {

        final String GEXF_FILE = System.getProperty("user.dir") + "/src/data/LesMiserables.gexf";

        // Carrega o arquivo .gexf
        File file = new File(GEXF_FILE);

        // Cria um DocumentBuilder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Converte o .gexf
        Document gexfDoc = builder.parse(file);

        NodeList nodes = gexfDoc.getElementsByTagName("node");
        int nodesCount = nodes.getLength();

        NodeList edges = gexfDoc.getElementsByTagName("edge");
        int edgesCount = edges.getLength();

        Graph graph = new Graph(nodesCount);

        for (int i = 0; i < edgesCount; i++) {
            Node edge = edges.item(i);

            if (edge.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) edge;
                Integer source = (int) Float.parseFloat(element.getAttribute("source"));
                Integer target = (int) Float.parseFloat(element.getAttribute("target"));

                graph.addEdge(source, target);
            }
        }

        return graph;
    }

    private static double[] calculateBetweennessCentrality(Graph graph) {
        // 1. Calcular o número total de pares de nós únicos
        // 2. Para cada nó, calcular o número total de caminhos mais curtos
        // 3. Para cada par de nós, calcular o número de caminhos mais curtos entre eles

        int verticesCount = graph.V();

        double values[] = new double[verticesCount];
        int source = 0;
        BreadthFirstPaths bfp = new BreadthFirstPaths(graph, source);

        bfp.computeSpCount(graph, source);
        for (int v = 0; v < verticesCount; v++) {            
            values[v] = bfp.calculateBetweennessCentrality(graph, v);
        }

        return values;
    }
}
