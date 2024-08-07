import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ListNode {
    int val;
    ListNode next;
    
    ListNode(int val) {
        this.val = val;
    }
}

class BusService {
    public ListNode optimizeBoardingProcess(ListNode head, int k) {
        // If the list is empty or k is 1, no change is needed
        if (head == null || k == 1) {
            return head;
        }

        // Dummy node to handle edge cases
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        ListNode curr = head;

        while (curr != null) {
            ListNode start = curr;
            ListNode end = getKthNode(curr, k);

            // If we don't have k nodes left, break the loop
            if (end == null) {
                break;
            }

            // Save the next group's start
            ListNode nextGroupStart = end.next;

            // Reverse the current group
            reverseGroup(start, nextGroupStart);

            // Connect the reversed group
            prev.next = end;
            start.next = nextGroupStart;

            // Move pointers
            prev = start;
            curr = nextGroupStart;
        }

        return dummy.next;
    }

    private ListNode getKthNode(ListNode node, int k) {
        while (node != null && k > 1) {
            node = node.next;
            k--;
        }
        return node;
    }

    private void reverseGroup(ListNode start, ListNode end) {
        ListNode prev = null;
        ListNode curr = start;
        while (curr != end) {
            ListNode nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }
    }

    // Helper method to create a linked list from an array
    public static ListNode createList(int[] arr) {
        if (arr == null || arr.length == 0) return null;
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        for (int val : arr) {
            current.next = new ListNode(val);
            current = current.next;
        }
        return dummy.next;
    }

    // Helper method to convert a linked list to an array
    public static int[] listToArray(ListNode head) {
        List<Integer> list = new ArrayList<>() ;
        while (head != null) {
            list.add(head.val);
            head = head.next;
        }
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    public static void main(String[] args) {
        BusService solution = new BusService();

        // Example 1
        int[] arr1 = {1, 2, 3, 4, 5};
        ListNode head1 = createList(arr1);
        ListNode result1 = solution.optimizeBoardingProcess(head1, 2);
        System.out.println("Example 1 Output: " + Arrays.toString(listToArray(result1)));

        // Example 2
        int[] arr2 = {1, 2, 3, 4, 5};
        ListNode head2 = createList(arr2);
        ListNode result2 = solution.optimizeBoardingProcess(head2, 3);
        System.out.println("Example 2 Output: " + Arrays.toString(listToArray(result2)));
    }
}