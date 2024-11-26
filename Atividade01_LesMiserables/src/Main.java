import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lib.Graph;
import lib.StdOut;

public class Main {

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

    public static void main(String[] args) throws Exception {
        Graph graph = readGraphFromGexf();

        StdOut.println(graph);
    }
}
