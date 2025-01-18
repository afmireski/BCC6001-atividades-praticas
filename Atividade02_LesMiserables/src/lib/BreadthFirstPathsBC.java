package lib;

import java.util.*;

public class BreadthFirstPathsBC {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked; // marked[v] = is there an s-v path
    private int[] distTo; // distTo[v] = number of edges shortest s-v path
    private double[] betweennessCentrality; // Betweenness centrality of each vertex

    public BreadthFirstPathsBC(Graph G) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        betweennessCentrality = new double[G.V()];
        Arrays.fill(betweennessCentrality, 0.0);

        // Calculate betweenness centrality for all vertices
        for (int s = 0; s < G.V(); s++) {
            bfsWithBetweenness(G, s);
        }

        // Normalize betweenness centrality (divide by 2 for undirected graphs)
        for (int v = 0; v < G.V(); v++) {
            betweennessCentrality[v] /= 2.0;
        }
    }

    private void bfsWithBetweenness(Graph G, int s) {
        LinkedList<Integer> queue = new LinkedList<>();
        Stack<Integer> stack = new Stack<>(); // To track the order of processing
        int[] sigma = new int[G.V()];         // Number of shortest paths passing through each vertex
        double[] delta = new double[G.V()];   // Dependency values for vertices

        // Initialize
        Arrays.fill(sigma, 0);
        Arrays.fill(delta, 0.0);
        Arrays.fill(marked, false);
        Arrays.fill(distTo, INFINITY);
        sigma[s] = 1;
        distTo[s] = 0;
        marked[s] = true;
        queue.add(s);

        // BFS: Calculate shortest paths and distances
        while (!queue.isEmpty()) {
            int v = queue.poll();
            stack.push(v);

            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    distTo[w] = distTo[v] + 1;
                    queue.add(w);
                }
                if (distTo[w] == distTo[v] + 1) { // Found a shortest path
                    sigma[w] += sigma[v];
                }
            }
        }

        // Bottom-up: Calculate dependencies
        while (!stack.isEmpty()) {
            int w = stack.pop();
            for (int v : G.adj(w)) {
                if (distTo[v] == distTo[w] - 1) {
                    delta[v] += ((double) sigma[v] / sigma[w]) * (1 + delta[w]);
                }
            }
            if (w != s) {
                betweennessCentrality[w] += delta[w];
            }
        }
    }

    public double[] getBetweennessCentrality() {
        return betweennessCentrality;
    }

    public static void main(String[] args) {
        // Test example
        In in = new In(args[0]);
        Graph G = new Graph(in);

        BreadthFirstPathsBC bfp = new BreadthFirstPathsBC(G);
        double[] bcs = bfp.getBetweennessCentrality();

        // Print the betweenness centrality for each vertex
        for (int v = 0; v < G.V(); v++) {
            System.out.printf("Vertex %d: Betweenness Centrality = %.4f\n", v, bcs[v]);
        }
    }
}
