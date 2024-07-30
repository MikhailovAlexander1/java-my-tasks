package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Methods {
    BufferedReader reader;
    ArrayList<String> data = new ArrayList<>();

    private final Map<String, String[]> r = new HashMap<>() {{
        put("*", new String[]{"<em>", "</em>"});
        put("_", new String[]{"<em>", "</em>"});
        put("**", new String[]{"<strong>", "</strong>"});
        put("-", new String[]{"-"});
        put("__", new String[]{"<strong>", "</strong>"});
        put("--", new String[]{"<s>", "</s>"});
        put("`", new String[]{"<code>", "</code>"});
        put("%", new String[]{"<var>", "</var>"});
    }};

    public Methods(File file) throws FileNotFoundException {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
    }

    private final StringBuilder sb = new StringBuilder();

    public String helper() throws IOException {
        StringBuilder out = new StringBuilder();
        while (fill()) {
            StringBuilder forOut = toHtml();
            identify(forOut);
            out.append(forOut);
            sb.setLength(0);
        }
        return out.toString();
    }

    private boolean fill() throws IOException {
        String buf;
        boolean end = false;
        while ((buf = reader.readLine()) != null) {
            if (!buf.isEmpty()) {
                sb.append(buf).append('\n');
                end = true;
            } else if (end) {
                return true;
            }
        }
        return end;
    }

    private void identify(StringBuilder text) {
        if (text.length() > 1) {
            if (text.charAt(0) == '#') {
                int index = 1;
                while (text.charAt(index) == '#') {
                    index++;
                }
                if (Character.isWhitespace(text.charAt(index))) {
                    text.delete(0, index + 1);
                    text.insert(0, "<h" + index + ">");
                    text.insert(text.length() - 1, "</h" + index + ">");
                } else {
                    fillPTag(text);
                }
            } else {
                fillPTag(text);
            }
        } else {
            fillPTag(text);
        }
    }

    private void fillPTag(StringBuilder text) {
        text.insert(0, "<p>");
        text.insert(text.length() - 1, "</p>");
    }

    private StringBuilder toHtml() throws IOException {
        BufferedReader sr = new BufferedReader(new StringReader(sb.toString()));
        StringBuilder tohtml = new StringBuilder();
        int el = sr.read();
        while (el != -1) {
            if ((char) el == '<' || (char) el == '>' || (char) el == '&') {
                if ((char) el == '<') {
                    tohtml.append("&lt;");
                } else if ((char) el == '>') {
                    tohtml.append("&gt;");
                } else {
                    tohtml.append("&amp;");
                }
            } else if (r.containsKey(String.valueOf((char) el))) {
                String tag = String.valueOf((char) el);
                el = sr.read();
                if (r.containsKey(tag + (char) el)) {
                    tag = tag + (char) el;
                    el = sr.read();
                }
                if ((tohtml.isEmpty() || !Character.isWhitespace(tohtml.charAt(tohtml.length() - 1)))
                    && !data.isEmpty() && data.get(data.size() - 1).equals(tag)) {
                    tohtml.append(r.get(tag)[1]);
                    data.remove(data.size() - 1);
                } else if (!Character.isWhitespace((char) el)) {
                    data.add(tag);
                    tohtml.append(r.get(tag)[0]);
                } else {
                    tohtml.append(tag);
                }
                continue;
            } else if ((char) el == '\\') {
                el = sr.read();
                tohtml.append((char) el);
            } else {
                tohtml.append((char) el);
            }
            el = sr.read();
        }
        return tohtml;
    }

    public void close() throws IOException {
        reader.close();
    }
}