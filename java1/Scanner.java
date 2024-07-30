import java.io.*;

public class Scanner {
    private final BufferedReader reader;
    private final StringBuilder buffer = new StringBuilder();
    private int countLines = 1;

    public Scanner() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public Scanner(String name, String encoding) throws FileNotFoundException, UnsupportedEncodingException {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(name), encoding));
    }

    public boolean hasNext() throws IOException {
        int symbol = reader.read();
        while (!Character.isWhitespace((char) symbol) || buffer.isEmpty()) {
            if ((char) symbol == '\n' && buffer.isEmpty()) {
                buffer.append((char) symbol);
                this.countLines++;
                break;
            }
            if (Character.isDigit((char) symbol) || Character.isLetter((char) symbol)
                    || Character.getType((char) symbol) == Character.DASH_PUNCTUATION || (char) symbol == '\'') {
                buffer.append((char) symbol);
            } else {
                if (!buffer.isEmpty()) {
                    break;
                }
            }
            if (symbol == -1 && buffer.isEmpty()) {
                return false;
            }
            symbol = reader.read();
            if ((char) symbol == '\n' && !buffer.isEmpty()) {
                this.countLines++;
                break;
            }
        }
        return true;
    }

    public String next() throws IOException {
        String out = buffer.toString();
        buffer.setLength(0);
        return out;
    }

    public boolean hasNextInt() throws IOException {
        int symbol = reader.read();
        while (!Character.isWhitespace((char) symbol) || buffer.isEmpty()) {
            if ((char) symbol == '\n' && buffer.isEmpty()) {
                buffer.append((char) symbol);
                this.countLines++;
                break;
            }
            if (Character.isDigit((char) symbol) || (char) symbol == '-') {
                buffer.append((char) symbol);
            } else {
                if (!buffer.isEmpty()) {
                    break;
                }
            }
            if (symbol == -1 && buffer.isEmpty()) {
                return false;
            }
            symbol = reader.read();
            if ((char) symbol == '\n' && !buffer.isEmpty()) {
                this.countLines++;
                break;
            }
        }
        return true;
    }

    public int nextInt() throws IOException {
        int out = Integer.parseInt(buffer.toString());
        buffer.setLength(0);
        return out;
    }

    public int getCountLines() {
        return this.countLines;
    }

    public void close() throws IOException {
        reader.close();
    }
}