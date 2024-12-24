import java.util.*;

// Graph sınıfı
class Graph {
    private int vertices; // Düğüm sayısı
    private List<List<Edge>> adjList; // Komşuluk listesi
    private List<Edge> edges; // Kenar listesi

    // Kenar sınıfı
    static class Edge {
        int source, destination, weight;

        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    // Graph constructor
    public Graph(int vertices) {
        this.vertices = vertices;
        adjList = new ArrayList<>();
        edges = new ArrayList<>();

        for (int i = 0; i < vertices; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    // Kenar ekleme metodu
    public void addEdge(int source, int destination, int weight) {
        adjList.get(source).add(new Edge(source, destination, weight));
        edges.add(new Edge(source, destination, weight));
    }

    // BFS algoritması
    public void bfs(int startVertex) {
        boolean[] visited = new boolean[vertices];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(startVertex);
        visited[startVertex] = true;

        System.out.print("BFS from vertex " + startVertex + ": ");
        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            System.out.print(vertex + " ");
            for (Edge edge : adjList.get(vertex)) {
                if (!visited[edge.destination]) {
                    visited[edge.destination] = true;
                    queue.add(edge.destination);
                }
            }
        }
        System.out.println();
    }

    // Döngü algılama metodu
    public void detectCycle() {
        boolean[] visited = new boolean[vertices];
        boolean[] recStack = new boolean[vertices];

        for (int i = 0; i < vertices; i++) {
            if (detectCycleUtil(i, visited, recStack)) {
                System.out.println("Cycle detected in the graph.");
                return;
            }
        }
        System.out.println("No cycle detected in the graph.");
    }

    private boolean detectCycleUtil(int vertex, boolean[] visited, boolean[] recStack) {
        if (recStack[vertex]) {
            return true;
        }
        if (visited[vertex]) {
            return false;
        }
        visited[vertex] = true;
        recStack[vertex] = true;

        for (Edge edge : adjList.get(vertex)) {
            if (detectCycleUtil(edge.destination, visited, recStack)) {
                return true;
            }
        }
        recStack[vertex] = false;
        return false;
    }

    // Dijkstra algoritması
    public void dijkstra(int startVertex) {
        int[] distances = new int[vertices];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[startVertex] = 0;

        boolean[] visited = new boolean[vertices];

        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        pq.add(new Edge(startVertex, startVertex, 0));

        while (!pq.isEmpty()) {
            Edge edge = pq.poll();
            int vertex = edge.destination;
            
            if (visited[edge.source])
                continue;

            visited[edge.source] = true;
            for (Edge neighbor : adjList.get(vertex)) {
                int newDist = distances[vertex] + neighbor.weight;
                if (newDist < distances[neighbor.destination]) {
                    distances[neighbor.destination] = newDist;
                    pq.add(new Edge(neighbor.source, neighbor.destination, newDist));
                }
            }
        }

        System.out.println("Shortest distances from vertex " + startVertex + ": " + Arrays.toString(distances));
    }

    // Bellman-Ford algoritması
    public void bellmanFord(int startVertex) {
        int[] distances = new int[vertices];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[startVertex] = 0;

        for (int i = 1; i < vertices; i++) {
            for (Edge edge : edges) {
                int newDist = distances[edge.source] + edge.weight;
                if (distances[edge.source] != Integer.MAX_VALUE && newDist < distances[edge.destination]) {
                    distances[edge.destination] = newDist;
                }
            }
        }

        // Negatif ağırlıklı döngü kontrolü
        for (Edge edge : edges) {
            int newDist = distances[edge.source] + edge.weight;
            if (distances[edge.source] != Integer.MAX_VALUE && newDist < distances[edge.destination]) {
                System.out.println("Graph contains a negative-weight cycle.");
                return;
            }
        }

        System.out.println("Shortest distances from vertex " + startVertex + " (Correct - Bellman-Ford): " + Arrays.toString(distances));
    }
}

// Main sınıfı
public class GraphAlgorithms {
    public static void main(String[] args) {
        Graph graph = new Graph(3); // Graf, 6 düğümle tanımlandı.

        // Kenarları ekleme
        graph.addEdge(0, 1, 3);
        graph.addEdge(0, 2, 5);
        graph.addEdge(2, 1, -3);


        // BFS ile graf taraması
        graph.bfs(0);

        // Döngü algılama
        graph.detectCycle();

        // Hatalı Dijkstra
        System.out.println("Running Dijkstra (with possible errors due to negative weights):");
        graph.dijkstra(0);

        // Doğru Bellman-Ford
        System.out.println("\nRunning Bellman-Ford (correct algorithm for graphs with negative weights):");
        graph.bellmanFord(0);
    }
}