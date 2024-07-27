public class SecretDecoderRing {
    public static String decodeMessage(String s, int[][] shifts) {
        char[] message = s.toCharArray();
        
        for (int[] shift : shifts) {
            int start = shift[0];
            int end = shift[1];
            int direction = shift[2];
            
            for (int i = start; i <= end; i++) {
                if (direction == 1) {
                    message[i] = (char) ((message[i] - 'a' + 1) % 26 + 'a');
                } else {
                    message[i] = (char) ((message[i] - 'a' + 25) % 26 + 'a');
                }
            }
        }
        
        return new String(message);
    }

    public static void main(String[] args) {
        String s = "hello";
        int[][] shifts = {{0, 1, 1}, {2, 3, 0}, {0, 2, 1}};
        
        String decodedMessage = decodeMessage(s, shifts);
        System.out.println("Decoded message: " + decodedMessage);
    }
}