import java.util.*;

class Friends {
    private int[] parent;
    private Set<Integer>[] restrictions;

    public List<String> friendRequests(int n, int[][] restrictions, int[][] requests) {
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        this.restrictions = new HashSet[n];
        for (int i = 0; i < n; i++) {
            this.restrictions[i] = new HashSet<>();
        }
        for (int[] restriction : restrictions) {
            this.restrictions[restriction[0]].add(restriction[1]);
            this.restrictions[restriction[1]].add(restriction[0]);
        }

        List<String> result = new ArrayList<>();
        for (int[] request : requests) {
            if (canBeFriends(request[0], request[1])) {
                union(request[0], request[1]);
                result.add("approved");
            } else {
                result.add("denied");
            }
        }

        return result;
    }

    private boolean canBeFriends(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return true;
        }

        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.offer(rootX);
        visited.add(rootX);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            if (restrictions[current].contains(rootY)) {
                return false;
            }

            for (int neighbor = 0; neighbor < parent.length; neighbor++) {
                if (find(neighbor) == current && !visited.contains(neighbor)) {
                    queue.offer(neighbor);
                    visited.add(neighbor);
                }
            }
        }

        return true;
    }

    private int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    private void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX != rootY) {
            parent[rootY] = rootX;
        }
    }

    public static void main(String[] args) {
        Friends solution = new Friends();

        // Example 1
        int n1 = 3;
        int[][] restrictions1 = {{0, 1}};
        int[][] requests1 = {{0, 2}, {2, 1}};
        System.out.println(solution.friendRequests(n1, restrictions1, requests1));
       

        // Example 2
        int n2 = 5;
        int[][] restrictions2 = {{0, 1}, {1, 2}, {2, 3}};
        int[][] requests2 = {{0, 4}, {1, 2}, {3, 1}, {3, 4}};
        System.out.println(solution.friendRequests(n2, restrictions2, requests2));
    }
}