package info.kgeorgiy.ja.mikhailov.walk;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Walk {
    private static final String ZEROS = ("0".repeat(64));
    public static void main(String[] args) {
        if (args == null) {
            System.err.println("Command line has to have arguments!");
        } else if (args.length < 2) {
            System.err.println("Command line has to have 2 arguments: \"input.file\" and \"output.file\"");
        } else if (args[0] == null || args[1] == null) {
            System.err.println("Command line arguments mustn't be null!");
        } else {
            String inputFileName = args[0];
            String outputFileName = args[1];
            Path inputFilePath, outputFilePath, outputFileParent;
            try {
                inputFilePath = getPath(inputFileName);
                outputFilePath = getPath(outputFileName);
            } catch (InvalidPathException e) {
                System.err.println(e.getMessage());
                return;
            }
            try {
                outputFileParent = outputFilePath.getParent();
            } catch (InvalidPathException e) {
                System.err.println("Cannot find parent of this file: \"" + outputFileName + "\".");
                return;
            }
            if (outputFileParent != null) {
                try {
                    Files.createDirectories(outputFileParent);
                } catch (IOException e) {
                    System.err.println("Unexpected behavior while creating directories: \"" + outputFileParent + "\" " + outputFileName);
                    return;
                }
            }
            try (BufferedReader inputFileReader = Files.newBufferedReader(inputFilePath, StandardCharsets.UTF_8);
                 BufferedWriter outputFileWriter = Files.newBufferedWriter(outputFilePath, StandardCharsets.UTF_8)) {
                MessageDigest ms = MessageDigest.getInstance("SHA-256");
                String fileForHash = inputFileReader.readLine();
                while (fileForHash != null) {
                    String hash = getHashSumOfFile(fileForHash, ms);
                    outputFileWriter.write(String.format(hash + " " + fileForHash + "%n"));
                    fileForHash = inputFileReader.readLine();
                }
            } catch (IOException e) {
                System.err.println("Something broken while reading from/writing to file: " +
                        "(INPUT FILE: \"" + inputFileName + "\") " +
                        "(OUTPUT FILE: \"" + outputFileName + "\")");
            } catch (NoSuchAlgorithmException e) {
                System.err.println("Unsupported algorithm for MessageDigest.");
            }
        }
    }

    private static Path getPath(String fileName) {
        try {
            return Path.of(fileName);
        } catch (InvalidPathException e) {
            throw new InvalidPathException("cannot be converted to path", "File: \"" + fileName + "\"");
        }
    }

    private static String getHashSumOfFile(String inputFileName, MessageDigest ms) {
        Path inputFilePath;
        try {
            inputFilePath = Path.of(inputFileName);
        } catch (InvalidPathException e) {
            return ZEROS;
        }
        try (InputStream inputFile = Files.newInputStream(inputFilePath)) {
            byte[] data = new byte[4096];
            int read;
            ms.reset();
            while ((read = inputFile.read(data)) != -1) {
                ms.update(data, 0, read);
            }
            StringBuilder hashResult = new StringBuilder();
            byte[] hash = ms.digest();
            for (byte b : hash) {
                if ((0xff & b) < 0x10) {
                    hashResult.append("0").append(Integer.toHexString((0xFF & b)));
                } else {
                    hashResult.append(Integer.toHexString(0xFF & b));
                }
            }
            return hashResult.toString();
        } catch (IOException e) {
            return ZEROS;
        }
    }
}
