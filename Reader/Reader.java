public class Reader {
    private String data;
    private int currPos;
    private int dataLength;

    // Constructor
    public Reader(String str) {
        this.data = str;
        this.currPos = 0;
        this.dataLength = str.length();
    }

    // Method to get the next character
    public char nextChar() {
        if (currPos >= dataLength) {
            return (char) -1; // end of stream
        }
        return data.charAt(currPos++);
    }

    // Method to retract n characters
    public void retract(int n) {
        if (n == 0) {
            n = 1;
        }

        currPos -= n;
        if (currPos < 0) {
            currPos = 0;
        }
    }

    // Overloaded method to retract 1 character
    public void retract() {
        retract(1);
    }

    public static void main(String[] args) {
        Reader reader = new Reader("Hello, world!");

        // Read and print characters one by one
        for (int i = 0; i < 5; i++) {
            System.out.print(reader.nextChar());
        }

        // Retract 2 characters
        reader.retract(2);

        // Read and print characters again
        for (int i = 0; i < 5; i++) {
            System.out.print(reader.nextChar());
        }
    }
}
