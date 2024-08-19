package info.kgeorgiy.ja.nazarov.walk;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class ProcessFiles {
    private static final Charset ENCODING = StandardCharsets.UTF_8;

    public static void readWalkWrite(String input, String output) {
        if (input == null || output == null) {
            System.out.println("Wrong args. There are no any paths to files");
            return;
        }
        Path pathInput = setPath(input);
        Path pathOutput = setPath(output);
        if (pathInput == null || pathOutput == null) {
            return;
        }
        if (pathOutput.getParent() != null) {
            try {
                Files.createDirectories(pathOutput.getParent());
            } catch (IOException e) {
                errorOut("Couldn't create path folders for output file. ERROR: ", e);
                return;
            }
        }
        try (BufferedReader reader = Files.newBufferedReader(pathInput, ENCODING)) {
            try (BufferedWriter writer = Files.newBufferedWriter(pathOutput, ENCODING)) {
                walkInFiles(reader, writer);
            } catch (FileNotFoundException e) {
                errorOut("There is no such write file. ERROR: ", e);
            } catch (SecurityException | AccessDeniedException e) {
                errorOut("There is no permission to write in such write file. ERROR: " +
                        "Security manager doesn't give abilities. ERROR: ", e);
            } catch (IOException e) {
                errorOut("Unknown exception, file can't be read or write. ERROR: ", e);
            }
        } catch (FileNotFoundException e) {
            errorOut("There is no such read file. ERROR: ", e);
        } catch (SecurityException | AccessDeniedException e) {
            errorOut("There is no permission to read in such read file. ERROR: " +
                    "Security manager doesn't give abilities. ERROR: ", e);
        } catch (IOException e) {
            errorOut("Unknown exception, file can't be read. ERROR: ", e);
        }
    }

    private static void walkInFiles(BufferedReader reader, BufferedWriter writer) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            int hash = 0;
            try (InputStream txtReader = Files.newInputStream(Path.of(line))) {
                byte[] buffer = new byte[4096];
                int bytesCnt;
                while ((bytesCnt = txtReader.read(buffer)) != -1) {
                    hash = SumCounter.jankinsHashFirstState(buffer, hash, bytesCnt);
                }
                hash = SumCounter.jankinsHashFinalState(hash);
            } catch (InvalidPathException e) {
                errorOut("Wrong file path format in INPUT file, check file way. ERROR: ", e);
            } catch (FileNotFoundException e) {
                errorOut("There is no such read file in INPUT file. ERROR: ", e);
            } catch (SecurityException | AccessDeniedException e) {
                errorOut("There is no permission to read in such read file from INPUT FILE. " +
                        "Security manager doesn't give abilities. ERROR: ", e);
            } catch (IOException e) {
                errorOut("Unknown exception, file didn't count full. ERROR: ", e);
                hash = 0;
            } finally {
                writer.write(String.format("%08x %s%n", hash, line));
            }
        }
    }

    private static Path setPath(String file) {
        Path path = null;
        try {
            path = Path.of(file);
        } catch (InvalidPathException e) {
            errorOut("Wrong -|| " + file + " ||- path format, check file way. ERROR: ", e);
        }
        return path;
    }

    private static void errorOut(String message, Exception e) {
        System.err.println(message + e.getMessage());
    }
}
