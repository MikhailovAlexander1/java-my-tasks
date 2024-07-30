import java.io.IOException;
import java.util.Arrays;

public class ReverseAbc2 {
    public static void main(String[] args) {
        int[] numbers = new int[10];
        int[] countNumbersLine = new int[10];
        int i = 0;
        int j = 0;
        int previousCount = 0;
        int bufferValue = 0;
        int numberOfLine = 1;
        try {
            Scanner ss = new Scanner();
            try {
                while (ss.hasNext()) {
                    String aa = ss.next();
                    if (Character.isLetter(aa.charAt(0)) || aa.charAt(0) == '-') {
                        if (i + 1 > numbers.length) {
                            numbers = Arrays.copyOf(numbers, numbers.length * 2);
                        }
                        if (aa.charAt(0) == '-') {
                            for (int k = 1; k < aa.length(); k++) {
                                bufferValue = bufferValue * 10 + (aa.charAt(k) - 'a');
                            }
                            numbers[i] = bufferValue - 2 * bufferValue;
                        } else {
                            for (int k = 0; k < aa.length(); k++) {
                                bufferValue = bufferValue * 10 + (aa.charAt(k) - 'a');
                            }
                            numbers[i] = bufferValue;
                        }
                        bufferValue = 0;
                        i++;
                    }
                    if (numberOfLine != ss.getCountLines()) {
                        if (j + 1 > countNumbersLine.length) {
                            countNumbersLine = Arrays.copyOf(countNumbersLine, countNumbersLine.length * 2);
                        }
                        countNumbersLine[j] = i - previousCount;
                        j++;
                        previousCount = i;
                        numberOfLine = ss.getCountLines();
                    }
                }
            } finally {
                ss.close();
            }
        } catch (IOException e) {
            System.err.println("Cannot read data: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("data is null: " + e.getMessage());
        }

        while (j > 0) {
            if (countNumbersLine[j - 1] != 0) {
                for (int k = 0; k < countNumbersLine[j - 1]; k++) {
                    System.out.print(numbers[i - 1] + " ");
                    i--;
                }
            }
            System.out.println();
            j--;
        }
    }
}