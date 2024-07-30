package lab2;

import lab2.parser.Tree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GraphvizSupporter {
    private final BufferedWriter writer;
    private int count = 0;

    public GraphvizSupporter(String fileName) throws IOException {
        File file = new File(fileName);
        this.writer = Files.newBufferedWriter(file.toPath());
    }

    public void dfs(Tree tree) throws IOException {
        if (count == 0) {
            writer.append(String.format("0 [label=\"%s\"]\n", tree.getNode()));
        }
        int curCount = count;
        count += 1;
        for (Tree child : tree.getChildren()) {
            writer.append(String.format("%d [label=\"%s\"", count, child.getNode()));
            if (child.isTerminal()) {
                writer.append(",color=red");
            }
            writer.append("]\n").append(String.format("%d -> %d\n", curCount, count));
            dfs(child);
        }
    }

    private void compileDotFile(String graphDeclaration, String outputFile) throws IOException {
        new ProcessBuilder("dot", "-Tpng", graphDeclaration)
                .redirectOutput(new File(outputFile))
                .start();
    }

    public void printGraph(Tree root, String graphDeclaration, String fileName) throws IOException {
        writer.append("digraph {\n");
        dfs(root);
        writer.append("}");
        writer.close();
        compileDotFile(graphDeclaration, fileName);
    }
}
