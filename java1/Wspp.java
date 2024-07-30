import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Wspp {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        ScannerForWspp scan = new ScannerForWspp(args[0], "utf-8");
        Map<String, ArrayList<Integer>> r = new LinkedHashMap<>();
        int k = 1;
        try {
            try {
                while (scan.hasNext()) {
                    String word = scan.next().toLowerCase();
                    if (word.charAt(0) != '\n') {
                        if (r.containsKey(word)) {
                            r.get(word).set(0, r.get(word).get(0) + 1);
                        } else {
                            r.put(word, new ArrayList<>());
                            r.get(word).add(1);
                        }
                        r.get(word).add(k);
                        k++;
                    }
                }
            } finally {
                scan.close();
            }
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8))) {
                for (Map.Entry<String, ArrayList<Integer>> v : r.entrySet()) {
                    writer.write(v.getKey());
                    for (int i = 0; i < r.get(v.getKey()).size(); i++) {
                        writer.write(" " + r.get(v.getKey()).get(i));
                    }
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("cannot white data: " + e.getMessage());
        }
    }
}
