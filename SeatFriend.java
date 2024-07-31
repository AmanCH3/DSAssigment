public class SeatFriend {
    public boolean findSeatsForFriends(int[] nums, int indexDiff, int valueDiff) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length && j <= i + indexDiff; j++) {
                if (Math.abs(nums[i] - nums[j]) <= valueDiff) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        SeatFriend solution = new SeatFriend();

        // Test case 1 (Original example)
        int[] nums1 = {2, 3, 5, 4, 9};
        int indexDiff1 = 2;
        int valueDiff1 = 1;
        boolean result1 = solution.findSeatsForFriends(nums1, indexDiff1, valueDiff1);
        System.out.println("Test case 1 result: " + result1); // Expected: true

        // Test case 2 (Should return true)
        int[] nums2 = {1, 5, 3, 7, 9};
        int indexDiff2 = 2;
        int valueDiff2 = 2;
        boolean result2 = solution.findSeatsForFriends(nums2, indexDiff2, valueDiff2);
        System.out.println("Test case 2 result: " + result2); // Expected: true

        // Test case 3 (Should return false)
        int[] nums3 = {1, 10, 20, 30, 40};
        int indexDiff3 = 1;
        int valueDiff3 = 5;
        boolean result3 = solution.findSeatsForFriends(nums3, indexDiff3, valueDiff3);
        System.out.println("Test case 3 result: " + result3); // Expected: false
    }


} 
