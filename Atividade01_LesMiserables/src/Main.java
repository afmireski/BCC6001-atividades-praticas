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

        StdOut.println(graph);

        int[] eccentricities = calculateEccentricity(graph);
        System.out.println("Excentricidades: ");
        for (int i = 0; i < eccentricities.length; i++) {
            StdOut.println("Excentricidade do vértice " + i + ": " + eccentricities[i]);
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

    private static int[] calculateEccentricity(Graph graph) {
        // 1. Roda a busca em largura
        // 2. Obtém todas as distâncias mínimas a partir do vértice fonte para os outros vértices
        // 3. Obtém a maior distância mínima (excentricidade)

        int verticesCount = graph.V();

        int eccentricities[] = new int[verticesCount];
        for (int v = 0; v < verticesCount; v++) {
            BreadthFirstPaths bfp = new BreadthFirstPaths(graph, v);

            int eccentricity = bfp.greatestDist();

            eccentricities[v] = eccentricity;
        }

        return eccentricities;
    }
}
