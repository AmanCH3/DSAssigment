class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
}

public class Graph {
    public int maxMagicalGrove(TreeNode root, boolean isLeft) {
        if (root == null) return 0;
        int maxValue = root.val;
        if (isLeft) {
            maxValue = Math.max(maxValue, maxMagicalGrove(root.right, false));
        } else {
            maxValue = Math.max(maxValue, maxMagicalGrove(root.left, true));
        }

        return maxValue;
    }

    public int maxTotalValue(TreeNode root) {
        if (root == null) return 0;

        int leftMax = maxMagicalGrove(root.left, true);
        int rightMax = maxMagicalGrove(root.right, false);

        int totalValue = leftMax + rightMax + root.val;
        return totalValue;
    }

    public int maxMagicGroves(TreeNode root) {
        if (root == null) return 0;

        int maxTotalValue = maxTotalValue(root);
        int leftMax = maxMagicGroves(root.left);
        int rightMax = maxMagicGroves(root.right);

        return Math.max(maxTotalValue, Math.max(leftMax, rightMax));
    }

    public static void main(String[] args) {
        Graph graph = new Graph();

        // Constructing the binary tree
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(4);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(2);
        root.left.right = new TreeNode(4);
        root.right.left = new TreeNode(2);
        root.right.right = new TreeNode(5);

        int maxTotalValue = graph.maxMagicGroves(root);
        System.out.println("The maximum total value is: " + maxTotalValue);
    }
}
