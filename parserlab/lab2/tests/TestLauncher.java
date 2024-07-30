package lab2.tests;

import lab2.GraphvizSupporter;
import lab2.parser.Parser;
import lab2.parser.Tree;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

public class TestLauncher {
    private static class ANSI {
        public static final String RESET = "\u001B[0m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
    }

    private static String getErrorPointer(int pos) {
        StringBuilder sb = new StringBuilder();
        pos += 3;
        while (pos > 1) {
            sb.append(' ');
            pos--;
        }
        sb.append("^").append("\n");
        return sb.toString();
    }

    private static void makeTestFolder(String folderName) throws IOException {
        File folder = new File(folderName);
        if (!folder.mkdir() && !folder.exists()) {
            String[] pathParts = folder.toString().split("/");
            throw new IOException(String.format("Can not create folder for test - \"%s\".", pathParts[pathParts.length - 1]));
        }
    }

    private static void testRun(Field test, Field check) throws ParseException, IOException, IllegalAccessException {
        String testFolder = String.format("/Users/admikhailov/IdeaProjects/MT/src/lab2/graphs/%s", test.getName());
        makeTestFolder(testFolder);
        String[] inputs = (String[]) test.get(Test.class);
        int[] expectedResults = (int[]) check.get(Test.class);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("========================%s========================%n", test.getName()));
        int fails = 0;
        int result;
        for (int i = 0; i < inputs.length; i++) {
            Parser parser = new Parser(new ByteArrayInputStream(inputs[i].getBytes(StandardCharsets.UTF_8)));
            sb.append(String.format("%n%d.  ", i + 1)).append(String.format("input: \"%s\"%n", inputs[i]));
            if (expectedResults[i] == 0) {
                sb.append(String.format("\t%sEXPECTED: Success%s%n", ANSI.GREEN, ANSI.RESET));
            } else {
                sb.append(String.format("\t%sEXPECTED: ParseException%s%n", ANSI.RED, ANSI.RESET));
            }
            try {
                Tree parseResult = parser.parse();
                String graphDeclaration = String.format("/%s/gr%d", testFolder, i);
                String outputFile = String.format("/%s/png%d.png", testFolder, i);
                GraphvizSupporter graphvizSupporter = new GraphvizSupporter(graphDeclaration);
                graphvizSupporter.printGraph(parseResult, graphDeclaration, outputFile);
                sb.append(String.format("\t%sACTUAL: Success%s%n", ANSI.GREEN, ANSI.RESET));
                result = 0;
            } catch (ParseException e) {
                String error = String.format("\tACTUAL: %s%s -> \"",
                        "ParseException: ", e.getMessage());
                sb.append(String.format("%s%s%s\"%n%s%s",
                        ANSI.RED, error, inputs[i],
                        getErrorPointer(e.getErrorOffset() + error.length()), ANSI.RESET));
                result = 1;
            }
            if (result != expectedResults[i]) {
                fails++;
            }
        }
        if (fails == 0) {
            sb.append(String.format("%n____%s PASSED____", test.getName()));
        } else {
            sb.append(String.format("%n____%s FAILED; ok: %d failed: %d____",
                    test.getName(), inputs.length - fails, fails));
        }
        System.out.println(sb.append('\n'));
    }

    public static void testsLaunch(Class<?> testClass) throws ParseException, IOException, IllegalAccessException {
        Field[] tests = testClass.getFields();
        for (int i = 0; i < tests.length; i += 2) {
            testRun(tests[i], tests[i + 1]);
        }
    }
}
