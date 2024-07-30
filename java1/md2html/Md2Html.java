package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.nio.file.NoSuchFileException;

public class Md2Html {
    public static void main(String[] args) {
        try {
            File file = new File(args[0]);
            Methods m = new Methods(file);
            String out = m.helper();
            m.close();
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8))) {
                writer.write(out);
            }
        } catch (NoSuchFileException e) {
            System.out.println(e.getMessage());
        } catch (FileSystemException e) {
            System.out.println("Файл занят другим процессов: " + e.getMessage() + " | " + e.getCause() + " | " + e.getReason());
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find file: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Cannot read data: " + e.getMessage());
        }
    }
}