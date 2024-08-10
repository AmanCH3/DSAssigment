import java.util.Arrays;
import java.util.Random;

public class TspHillClimber {
    private int numCities;
    private double[][] distances; // adjacency matrix
    private boolean[][] visited; // keeps track of visited cities
    private double fitness; // current fitness value

    public TspHillClimber(int numCities) {
        this.numCities = numCities;
        distances = new double[numCities][numCities];
        visited = new boolean[numCities][numCities];
    }

    public void setDistances(double[][] distances) {
        this.distances = distances;
    }

    public void solve() {
        // initialize solution with random order
        int[] order = new int[numCities];
        for (int i = 0; i < numCities; i++) {
            order[i] = i;
        }
        shuffle(order);

        // calculate initial fitness value
        fitness = calculateFitness(order);

        // hill climbing algorithm
        while (true) {
            int[] newOrder = getNeighbor(order);
            double newFitness = calculateFitness(newOrder);

            if (newFitness > fitness) { // improvement found
                order = newOrder;
                fitness = newFitness;
            } else { // no improvement, stop
                break;
            }
        }

        System.out.println("Optimal tour: " + Arrays.toString(order));
        System.out.println("Total distance: " + fitness);
    }

    private int[] getNeighbor(int[] order) {
        // random swap two cities in the order
        int i = (int) (Math.random() * numCities);
        int j = (int) (Math.random() * numCities);
        while (i == j) { // ensure distinct cities
            j = (int) (Math.random() * numCities);
        }

        int[] newOrder = order.clone();
        swap(newOrder, i, j);

        return newOrder;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private double calculateFitness(int[] order) {
        double totalDistance = 0;

        for (int i = 0; i < numCities - 1; i++) {
            totalDistance += distances[order[i]][order[i + 1]];
        }

        // add distance from last city back to first city
        totalDistance += distances[order[numCities - 1]][order[0]];

        return totalDistance;
    }

    private void shuffle(int[] arr) {
        Random rand = new Random();
        for (int i = arr.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            swap(arr, i, index);
        }
    }

    public static void main(String[] args) {
        // example TSP instance
        double[][] distances = new double[][] {
                {0, 10, 15, 20},
                {10, 0, 25, 18},
                {15, 25, 0, 22},
                {20, 18, 22, 0}
        };

        TspHillClimber tsp = new TspHillClimber(4);
        tsp.setDistances(distances);
        tsp.solve();
    }
}
