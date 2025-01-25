package lib;

import java.util.*;

public class BreadthFirstPathsBC {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked; // marked[v] = se é um caminho entre s-v
    private int[] distTo; // distTo[v] = número de caminhos mais curtos entre s-v
    private double[] betweennessCentrality; // Betweenness centrality de cada vértice

    public BreadthFirstPathsBC(Graph G) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        betweennessCentrality = new double[G.V()];
        Arrays.fill(betweennessCentrality, 0.0);

        // Calcular a betweenness centrality de todos os vértices
        for (int s = 0; s < G.V(); s++) {
            bfsWithBetweenness(G, s);
        }

        // Normalização da betweenness centrality (dividir por dois no caso de grafos de não direcionados)
        for (int v = 0; v < G.V(); v++) {
            betweennessCentrality[v] /= 2.0;
        }
    }

    private void bfsWithBetweenness(Graph G, int s) {
        LinkedList<Integer> queue = new LinkedList<>();
        Stack<Integer> stack = new Stack<>(); // Fila com a ordem de processamento dos nós
        int[] sigma = new int[G.V()];         // Número de caminhos mais curtos que passam por cada vértice
        double[] delta = new double[G.V()];   // Valores de dependência acumulada de cada vértice (Grau de importância)

        // Inicialização
        Arrays.fill(sigma, 0);
        Arrays.fill(delta, 0.0);
        Arrays.fill(marked, false);
        Arrays.fill(distTo, INFINITY);
        sigma[s] = 1;
        distTo[s] = 0;
        marked[s] = true;
        queue.add(s);

        // BFS: Calcula menores caminhos e distâncias
        while (!queue.isEmpty()) {
            int v = queue.poll();
            stack.push(v);

            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    distTo[w] = distTo[v] + 1;
                    queue.add(w);
                }
                if (distTo[w] == distTo[v] + 1) { // Encontrou um caminho mais curto
                    sigma[w] += sigma[v];
                }
            }
        }

        // Bottom-up: Calcula os graus de importância de cada vértice 
        // em relação aos outros vértices, começando dos mais distantes para os mais próximos
        while (!stack.isEmpty()) {
            int w = stack.pop();
            for (int v : G.adj(w)) { // Verifica se v é predecessor de w
                if (distTo[v] == distTo[w] - 1) {
                    delta[v] += ((double) sigma[v] / sigma[w]) * (1 + delta[w]); // Calcula o grau de importância de v
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
