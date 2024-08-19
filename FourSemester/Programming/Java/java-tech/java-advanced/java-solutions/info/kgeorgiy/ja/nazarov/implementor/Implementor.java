package info.kgeorgiy.ja.nazarov.implementor;

import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Implementation of the {@link JarImpler} interface. Provides the ability to create an interface implementation
 * and compiling it into a JAR file.
 */
public class Implementor implements JarImpler {
    /**
     * Enumeration for storing used tokens and templates.
     * Used to simplify code generation and ensure uniform formatting.
     */
    private enum TokensLib {
        /**
         * Suffix for naming of generated interface.
         */
        GEN_SUFF("Impl"),

        /**
         * Java file format for creating.
         */
        FORMAT_JAVA("java"),

        /**
         * Class file format for creating.
         */
        FORMAT_CLASS("class"),

        /**
         * The newline character according to the system settings.
         */
        NEXT_LINE(System.lineSeparator()),

        /**
         * Double line feed to separate code blocks.
         */
        SKIP_LINE(NEXT_LINE.value + NEXT_LINE.value),

        /**
         * Indentation for formatting the code.
         */
        PRE_STEP("    ");

        /**
         * The token value used when generating the code.
         */
        private final String value;

        /**
         * Constructor for enumeration elements {@link TokensLib}.
         *
         * @param value String representation of the token.
         */
        TokensLib(String value) {
            this.value = value;
        }

        /**
         * Returns string representation of the token
         */
        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * The default constructor for {@code Implementor}.
     * Creates an instance of the {@code Implementor} class that can be used
     * to generate interface implementations and create corresponding JAR files.
     */
    public Implementor() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        Path tempDir = jarFile.getParent();
        implement(token, tempDir);
        compileClass(token, tempDir);
        createJar(token, tempDir, jarFile);
    }

    /**
     * Compiles a class that implements the interface specified in the {@code token} parameter.
     *
     * @param token is the interface class for which you need to create an implementation.
     * @param root  is the path to the directory where the compiled class will be saved.
     * @throws ImplerException If compilation failed.
     */
    private void compileClass(Class<?> token, Path root) throws ImplerException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new ImplerException("Compiler is null");
        }
        String path;
        try {
            path = Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
        } catch (URISyntaxException e) {
            throw new ImplerException("Invalid path", e);
        }
        String filePath = root.resolve(getNameFile(token, TokensLib.FORMAT_JAVA)).toString();
        int compilationResult = compiler.run(null, null, null,
                "-cp", path, "-encoding", "UTF-8", filePath);
        if (compilationResult != 0) {
            throw new ImplerException("Compilation failed");
        }
    }

    /**
     * Creates a JAR file containing the compiled interface implementation class.
     *
     * @param token   The interface class for which the implementation was created.
     * @param tempDir is a temporary directory containing compiled classes.
     * @param jarFile is the path to the JAR file being created.
     * @throws ImplerException If JAR file creation failed.
     */
    private void createJar(Class<?> token, Path tempDir, Path jarFile) throws ImplerException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        try (JarOutputStream out = new JarOutputStream(new PrintStream(jarFile.toFile(), StandardCharsets.UTF_8), manifest)) {
            String entryName = getNameFile(token, TokensLib.FORMAT_CLASS);
            out.putNextEntry(new JarEntry(entryName));
            Files.copy(tempDir.resolve(entryName), out);
        } catch (IOException e) {
            throw new ImplerException("Cannot write jar file", e);
        }
    }

    /**
     * The entry point to the program.
     * Processes command line arguments and starts the process of creating an interface implementation
     * or creating a JAR file with the implementation.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        if (
                args == null || Arrays.stream(args).anyMatch(Objects::isNull)
                        || (args.length != 1 && !(args.length == 3 && args[0].equals("-jar")))
        ) {
            System.out.println("Usage:");
            System.out.println("  java -jar Implementor.jar <ClassName> to generate implementation");
            System.out.println("  java -jar Implementor.jar -jar <ClassName> <JarFileName> to generate JAR");
            return;
        }
        try {
            Implementor implementor = new Implementor();
            if (args.length == 1) {
                implementor.implement(Class.forName(args[0]), Paths.get("."));
            } else {
                implementor.implementJar(Class.forName(args[1]), Paths.get(args[2]));
            }
        } catch (ClassNotFoundException | ImplerException | InvalidPathException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        if (!token.isInterface()) {
            throw new ImplerException("Only interfaces are supported.");
        }
        if (Modifier.isPrivate(token.getModifiers())) {
            throw new ImplerException("Only public interfaces are supported.");
        }
        Path path = root.resolve(getNameFile(token, TokensLib.FORMAT_JAVA));
        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                writer.write(generateClass(token));
            }
        } catch (IOException e) {
            throw new ImplerException("Error writing to file: " + path, e);
        }
    }

    /**
     * Generates the code of the class implementing the specified interface.
     *
     * @param token is the interface class for which the implementation is being created.
     * @return A string containing the implementation class code.
     */
    private String generateClass(Class<?> token) {
        String packageName = token.getPackageName();
        String className = escapeUnicode(token.getSimpleName() + TokensLib.GEN_SUFF);
        String interfaceName = escapeUnicode(token.getCanonicalName());

        return String.format("package %s;%s", packageName, TokensLib.SKIP_LINE) +
                String.format("public class %s implements %s ", className, interfaceName) +
                String.format("{%s%s%s}%s", TokensLib.NEXT_LINE, generateMethods(token), TokensLib.NEXT_LINE, TokensLib.NEXT_LINE);
    }

    /**
     * Format string to utf format.
     *
     * @param input string to unicode transform.
     * @return String input in format.
     */
    private String escapeUnicode(String input) {
        StringBuilder b = new StringBuilder(input.length());
        for (char c : input.toCharArray()) {
            if (c >= 128)
                b.append(String.format("\\u%04X", (int) c));
            else
                b.append(c);
        }
        return b.toString();
    }

    /**
     * Generates method code for the implementation class.
     *
     * @param token is the interface class for which the implementation is being created.
     * @return A string containing the code of all methods of the implementation class.
     */
    private String generateMethods(Class<?> token) {
        return Arrays.stream(token.getMethods())
                .filter(method -> Modifier.isAbstract(method.getModifiers()))
                .map(this::generateMethod)
                .collect(Collectors.joining(TokensLib.SKIP_LINE.toString()));
    }

    /**
     * Generates code for a separate method.
     *
     * @param method The method for which the code is generated.
     * @return A string containing the method code.
     */
    private String generateMethod(Method method) {
        return String.format("%s@Override%s", TokensLib.PRE_STEP, TokensLib.NEXT_LINE) +
                String.format("public %s %s", method.getReturnType().getCanonicalName(), method.getName()) +
                String.format("(%s) ", generateMethodArgs(method)) +
                String.format("{%s%s%s}", TokensLib.NEXT_LINE, generateMethodBody(method), TokensLib.PRE_STEP);
    }

    /**
     * Generates the method body with the default return value.
     *
     * @param method The method for which heat is generated.
     * @return A string containing the method body.
     */
    private String generateMethodBody(Method method) {
        if (method.getReturnType().equals(Void.TYPE)) {
            return "";
        }
        return String.format("%s%sreturn %s;%s", TokensLib.PRE_STEP, TokensLib.PRE_STEP,
                getDefaultValue(method.getReturnType()), TokensLib.NEXT_LINE);
    }

    /**
     * Generates a string of arguments for the method.
     *
     * @param method The method whose arguments need to be generated.
     * @return String containing method arguments.
     */
    private String generateMethodArgs(Method method) {
        Class<?>[] args = method.getParameterTypes();
        return IntStream.range(0, args.length)
                .mapToObj(i -> String.format("%s %s", args[i].getCanonicalName(), (char) ('a' + i)))
                .collect(Collectors.joining(", "));
    }

    /**
     * Generates a string path to file with generated suffix and format.
     *
     * @param token  The interface class for which the implementation was created.
     * @param format File type from tokens lib.
     * @return String containing method arguments.
     */
    private String getNameFile(Class<?> token, TokensLib format) {
        return String.format("%s/%s.%s",
                token.getPackageName().replace('.', '/'),
                token.getSimpleName() + TokensLib.GEN_SUFF,
                format);
    }

    /**
     * Returns the default value for the specified type.
     *
     * @param returnType Type The type for which you want to get the default value.
     * @return A string containing the default value.
     */
    private String getDefaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive()) {
            return "null";
        }
        if (returnType.equals(Boolean.TYPE)) {
            return "false";
        }
        if (returnType.equals(Void.TYPE)) {
            return "";
        }
        return "0";
    }
}
