import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lib.BreadthFirstPathsBC;
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

    /**
     * O que é a Betweeness Centratility?
     * 
     * É uma medida de centralidade em grafos que indica 
     * o quanto um nó é usado como rota em caminhos mais curtos que levam a outros nós.
     * Pode-se presumir que quanto maior a centralidade de um nó, mais caminhos passam por ele,
     * o que indica que ele têm uma importância maior dentro do grafo.
     * 
     * Seu cálculo é dado pela divisão entre o número total de caminhos mais curtos entre os nós s e t, que passam pelo vértice v, e o número total de caminhos mais curtos entre os vértices s e t.
     */
    private static double[] calculateBetweennessCentrality(Graph graph) {

        BreadthFirstPathsBC bfp = new BreadthFirstPathsBC(graph);
        double bcs[] = bfp.getBetweennessCentrality();

        return bcs;
    }
}
