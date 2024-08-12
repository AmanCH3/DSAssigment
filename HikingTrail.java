public class HikingTrail {
    public static void main(String[] args) {
        int[] nums = {4, 2, 1, 4, 3, 4, 5, 8, 15};
        int k = 3;
        System.out.println(longestHike(nums, k));
    }

    public static int longestHike(int[] nums, int k) {
        int maxLen = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1]) {
                int len = 1;
                while (i + len < nums.length && nums[i + len] - nums[i + 
len - 1] <= k) {
                    len++;
                }
                maxLen = Math.max(maxLen, len);
            }
        }
        return maxLen;
    }
}

