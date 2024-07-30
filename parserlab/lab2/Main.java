package lab2;

import lab2.parser.Parser;
import lab2.parser.Tree;
import lab2.tests.Test;
import lab2.tests.TestLauncher;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws ParseException, IOException, IllegalAccessException {
//        tryParse("var hello : Array<HashMap<Int, String>>;");
        TestLauncher.testsLaunch(Test.class);
    }

    private static void tryParse(String toParse) throws ParseException, IOException {
        Parser parser = new Parser(new ByteArrayInputStream(toParse.getBytes(StandardCharsets.UTF_8)));
        Tree parseResult = parser.parse();
        String graphDeclaration = "src/lab2/graphs/gr";
        String outputFile = "src/lab2/graphs/main.png";
        GraphvizSupporter graphvizSupporter = new GraphvizSupporter(graphDeclaration);
        graphvizSupporter.printGraph(parseResult, graphDeclaration, outputFile);
    }
}
