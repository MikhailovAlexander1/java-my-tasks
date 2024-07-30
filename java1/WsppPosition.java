import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WsppPosition {
    public static void main(String[] args) {
        try {
            ScannerForWspp scan = new ScannerForWspp(args[0], "utf-8");
            Map<String, ArrayList<String>> r = new LinkedHashMap<>();
            Map<String, ArrayList<Integer>> p = new LinkedHashMap<>();
            int place = 1;
            int line = 0;
            try {
                while (scan.hasNext()) {
                    String word = scan.next().toLowerCase();
                    if (line != scan.getCountLines()) {
                        line++;
                        place = 1;
                    }
                    if (r.containsKey(word)) {
                        p.get(word).set(0, p.get(word).get(0) + 1);
                    } else {
                        r.put(word, new ArrayList<>());
                        p.put(word, new ArrayList<>());
                        p.get(word).add(1);
                    }
                    r.get(word).add(line + ":" + place);
                    place++;
                }
            } finally {
                scan.close();
            }
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8))) {
                for (Map.Entry<String, ArrayList<String>> v : r.entrySet()) {
                    writer.write(v.getKey());
                    writer.write(" " + p.get(v.getKey()).get(0));
                    for (int i = 0; i < r.get(v.getKey()).size(); i++) {
                        writer.write(" " + r.get(v.getKey()).get(i));
                    }
                    writer.newLine();
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("cannot find your file: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            System.err.println("cannot find your encoding: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("input data is null: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("cannot white data: " + e.getMessage());
        }
    }
}
