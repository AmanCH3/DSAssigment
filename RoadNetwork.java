import java.util.Arrays;

public class RoadNetwork {
    public static void main(String[] args) {
        int[][] graph = {{0, 1, -1}, {1, 2, -1}, {2, 3, -1}, {3, 0, -1}};
        int source = 0;
        int destination = 1;
        int targetTime = 5;

        modifyRoadTimes(graph, source, destination, targetTime);
    }

    public static void modifyRoadTimes(int[][] graph, int source, int destination, int targetTime) {
        int n = graph.length;
        // Create an array to store the shortest travel time from the source to each node
        int[] distances = new int[n];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[source] = 0;

        // Relax edges repeatedly
        for (int i = 0; i < n - 1; i++) {
            for (int[] edge : graph) {
                int u = edge[0];
                int v = edge[1];
                int w = edge[2];
                if (w == -1) w = 1; // Temporary weight for unconstructed roads
                if (distances[u] != Integer.MAX_VALUE && distances[u] + w < distances[v]) {
                    distances[v] = distances[u] + w;
                }
            }
        }

        // Check for negative-weight cycles
        boolean hasNegativeCycle = false;
        for (int[] edge : graph) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            if (w == -1) w = 1;
            if (distances[u] != Integer.MAX_VALUE && distances[u] + w < distances[v]) {
                hasNegativeCycle = true;
                break;
            }
        }

        if (hasNegativeCycle) {
            System.out.println("Error: Negative-weight cycle detected");
            return;
        }

        // Find the shortest path from source to destination with modified construction times
        boolean modified;
        do {
            modified = false;
            for (int[] edge : graph) {
                int u = edge[0];
                int v = edge[1];
                int w = edge[2];
                if (w == -1 && distances[u] != Integer.MAX_VALUE) {
                    int newTime = Math.min(2000000000, targetTime / (distances[u] + 1) + 1);
                    if (distances[u] + newTime < distances[v]) {
                        edge[2] = newTime;
                        distances[v] = distances[u] + newTime;
                        modified = true;
                    }
                }
            }
        } while (modified);

        if (distances[destination] <= targetTime) {
            System.out.println(Arrays.deepToString(graph));
        } else {
            System.out.println("It is not possible to reach the destination within the target time.");
        }
    }
}
