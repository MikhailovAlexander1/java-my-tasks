package info.kgeorgiy.ja.mikhailov.implementor;

import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Stream;

/**
 * A simple implementation of the {@link JarImpler} interface.
 */
public class Implementor implements JarImpler {
    /**
     * {@link HashSet} to save the signature of already implemented methods in the program process.
     */
    private final HashSet<MethodSupport> fileMethods = new HashSet<>();
    /**
     * {@link StringBuilder} to save provided class's realization.
     */
    private final StringBuilder classRealization = new StringBuilder();
    /**
     * Open bracket for generated <code>.java</code> files.
     */
    private final static String OPEN_BRACKET = "(";
    /**
     * Closed bracket for generated <code>.java</code> files.
     */
    private final static String CLOSED_BRACKET = ")";
    /**
     * Comma for generated <code>.java</code> files.
     */
    private final static String COMMA = ",";
    /**
     * End of line character for generated <code>.java</code> files.
     */
    private final static String SEMICOLON = ";";
    /**
     * Open brace for generated <code>.java</code> files.
     */
    private final static String OPEN_BRACE = "{";
    /**
     * Closed brace for generated <code>.java</code> files.
     */
    private final static String CLOSED_BRACE = "}";
    /**
     * Line separator for generated <code>.java</code> files.
     */
    private final static String LINE_TRANSLATION = System.lineSeparator();
    /**
     * Double line separator for generated <code>.java</code> files.
     */
    private final static String DOUBLE_LINE_TRANSLATION = LINE_TRANSLATION+LINE_TRANSLATION;
    /**
     * Space for generated <code>.java</code> files.
     */
    private final static String SPACE = " ";
    /**
     * <code>super</code> keyword for generated <code>.java</code> files.
     */
    private final static String SUPER = "super";
    /**
     * <code>throws</code> keyword for generated <code>.java</code> files.
     */
    private final static String THROWS = "throws";
    /**
     * <code>return</code> keyword for generated <code>.java</code> files.
     */
    private final static String RETURN = "return";
    /**
     * <code>package</code> keyword for generated <code>.java</code> files.
     */
    private final static String PACKAGE = "package";
    /**
     * <code>public</code> keyword for generated <code>.java</code> files.
     */
    private final static String PUBLIC = "public";
    /**
     * <code>class</code> keyword for generated <code>.java</code> files.
     */
    private final static String CLASS = "class";
    /**
     * <code>implements</code> keyword for generated <code>.java</code> files.
     */
    private final static String IMPLEMENTS = "implements";
    /**
     * <code>extends</code> keyword for generated <code>.java</code> files.
     */
    private final static String EXTENDS = "extends";
    /**
     * <code>.class</code> file extension.
     */
    private final static String CLASS_EXTENSION = ".class";
    /**
     * <code>.java</code> file extension.
     */
    private final static String JAVA_EXTENSION = ".java";
    /**
     * <code>Impl</code> additional file name for generated <code>.java</code> files.
     */
    private final static String ADDITIONAL_IMPL_NAME = "Impl";

    /**
     * Creating a new instance of the class.
     */
    public Implementor() {

    }

    /**
     * A {@link MethodSupport} is a support class for {@code fileMethods},
     * uses to compare method signature with custom {@link Object#equals(Object)}
     * and get hashcode with custom {@link Object#hashCode()}.
     */
    private static class MethodSupport {
        /**
         * The wrapped instance of {@link Method}.
         */
        private final Method method;
        /**
         * The base used for calculating {@link #hashCode()}.
         */
        private final static int BASE = 23;
        /**
         * The module used for calculating {@link #hashCode()}.
         */
        private final static int MOD = 1000992299;

        /**
         * Create a wrapper for the provided {@link Method method}.
         *
         * @param method the method to wrap.
         */
        public MethodSupport(Method method) {
            this.method = method;
        }

        /**
         * Compares 2 methods if they have same signature.
         *
         * @param object object with which to compare.
         * @return {@code true} if {@code object} is an instance of a {@link MethodSupport} and
         * methods have the same signature (method from which the method is called and {@code object}).
         */
        @Override
        public boolean equals(Object object) {
            if (object instanceof final MethodSupport other) {
                return Arrays.equals(method.getParameterTypes(), other.method.getParameterTypes())
                        && method.getName().equals(other.method.getName());
            }
            return false;
        }

        /**
         * Calculates hashcode for method from which the method is called.
         *
         * @return a hash code value for <code>this</code> object.
         */
        @Override
        public int hashCode() {
            return ((Arrays.hashCode(method.getParameterTypes())
                    + BASE) % MOD + method.getName().hashCode() * BASE * BASE) % MOD;
        }
    }

    /**
     * Returns default value for {@code type}.
     *
     * @param type Class for which need to get default value.
     * @return default value for {@code type}. If it is primitive returns default value for it, else {@code null}.
     */
    private String getDefaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return "null";
        } else {
            if (type.equals(boolean.class)) {
                return "false";
            } else if (type.equals(void.class)) {
                return "";
            } else {
                return "0";
            }
        }
    }

    /**
     * Writes parameters to generating <code>.java</code> file and returns their names (uses for java constructor).
     *
     * @param parameters an array of {@code parameters}.
     * @return {@link List<String>} of {@code parameters} names.
     */
    private List<String> writeParametersNamesAndReturnThem(Parameter[] parameters) {
        List<String> parameterNames = new ArrayList<>();
        classRealization.append(OPEN_BRACKET);
        for (Parameter p: parameters) {
            Class<?> pClazz = p.getType();
            String currParameterName = p.getName();
            parameterNames.add(currParameterName);
            classRealization.append(pClazz.getCanonicalName())
                    .append(SPACE)
                    .append(currParameterName)
                    .append(COMMA)
                    .append(SPACE);
        }
        if (parameters.length > 0) {
            classRealization.delete(classRealization.length() - 2, classRealization.length());
        }
        classRealization.append(CLOSED_BRACKET);
        return parameterNames;
    }

    /**
     * Writes methods realization (return type, method signature and default return value) to generating <code>.java</code> file.
     *
     * @param methods an array of {@code methods} to generate their realization.
     */
    private void writeMethodsRealization(Method[] methods) {
        for (Method method: methods) {
            int modifiers = method.getModifiers();
            if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                continue;
            }
            if (!fileMethods.add(new MethodSupport(method))) {
                continue;
            }

            Class<?> returnType = method.getReturnType();
            String returnValue = getDefaultValue(returnType);

            classRealization.append(System.lineSeparator())
                    .append(Modifier.toString(modifiers).split("\\s")[0])
                    .append(SPACE)
                    .append(returnType.getCanonicalName())
                    .append(SPACE).append(method.getName());
            writeParametersNamesAndReturnThem(method.getParameters());
            classRealization.append(SPACE)
                    .append(OPEN_BRACE)
                    .append(LINE_TRANSLATION)
                    .append(RETURN)
                    .append(SPACE)
                    .append(returnValue)
                    .append(SEMICOLON)
                    .append(LINE_TRANSLATION)
                    .append(CLOSED_BRACE);
        }
    }

    /**
     * Writes <code>public</code> methods (provides by {@link Class#getMethods()}) realizations
     * (uses {@link Implementor#writeMethodsRealization(Method[])} for it).
     *
     * @param clazz class from which <code>public</code> methods should be handled.
     */
    private void writePublicMethodsRealization(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        writeMethodsRealization(methods);
    }

    /**
     * Writes methods (provides by {@link Class#getDeclaredMethods()}) realizations
     * (uses {@link Implementor#writeMethodsRealization(Method[])} for it).
     *
     * @param clazz class from which declared methods should be handled.
     */
    private void writeDeclaredMethodsRealization(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        writeMethodsRealization(methods);
    }

    /**
     * Writes {@code clazz} constructors realizations to {@code newClassName}.
     *
     * @param clazz {@link Class} of the class whose implementation should be generated.
     * @param newClassName name of the class which should be generated.
     */
    private void writeConstructors(Class<?> clazz, String newClassName) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor: constructors) {
            int modifiers = constructor.getModifiers();
            if (Modifier.isPrivate(modifiers)) {
                continue;
            }
            classRealization.append(LINE_TRANSLATION)
                    .append(Modifier.toString(modifiers).split("\\s")[0])
                    .append(SPACE)
                    .append(newClassName);
            List<String> parametersNames = writeParametersNamesAndReturnThem(constructor.getParameters());
            Class<?>[] exceptions = constructor.getExceptionTypes();
            if (exceptions.length > 0) {
                classRealization.append(SPACE)
                        .append(THROWS)
                        .append(SPACE);
                for (Class<?> exception: exceptions) {
                    classRealization.append(exception.getCanonicalName())
                            .append(COMMA)
                            .append(SPACE);
                }
                classRealization.delete(classRealization.length() - 2, classRealization.length());
            }
            classRealization.append(SPACE)
                    .append(OPEN_BRACE)
                    .append(LINE_TRANSLATION)
                    .append(SUPER)
                    .append(OPEN_BRACKET);
            for (String parameterName: parametersNames) {
                classRealization.append(parameterName)
                        .append(COMMA)
                        .append(SPACE);
            }
            if (parametersNames.size() > 0) {
                classRealization.delete(classRealization.length() - 2, classRealization.length());
            }
            classRealization.append(CLOSED_BRACKET)
                    .append(SEMICOLON)
                    .append(LINE_TRANSLATION)
                    .append(CLOSED_BRACE)
                    .append(LINE_TRANSLATION);
        }
    }

    /**
     * Generates {@code clazz} realization to a file which handled by {@code writer}.
     *
     * @param clazz {@link Class} of the class whose implementation should be generated.
     * @param keyWord string value of keyword <code>implements</code> or <code>extends</code> depends on what program generate.
     * @param newClassName name of the class where {@code clazz} realization should be generated.
     * @param extendedClassName {@link Class#getCanonicalName()} of the class which realization should be generated.
     * @param writer {@link BufferedWriter} which handles file where realization will be written (file name of this file must match with {@code newClassName}).
     *
     * @throws IOException if something wrong while writing to file.
     */
    private void classGenerator(Class<?> clazz, String keyWord, String newClassName, String extendedClassName,
                                BufferedWriter writer) throws IOException {
        classRealization.append(PACKAGE + SPACE)
                .append(clazz.getPackageName())
                .append(SEMICOLON)
                .append(DOUBLE_LINE_TRANSLATION)
                .append(PUBLIC + SPACE + CLASS + SPACE)
                .append(newClassName)
                .append(SPACE)
                .append(keyWord)
                .append(SPACE)
                .append(extendedClassName)
                .append(SPACE)
                .append(OPEN_BRACE)
                .append(LINE_TRANSLATION);
        writeConstructors(clazz, newClassName);
        writePublicMethodsRealization(clazz);
        writeDeclaredMethodsRealization(clazz);
        writer.write(classRealization + LINE_TRANSLATION + CLOSED_BRACE);
        classRealization.setLength(0);
        fileMethods.clear();
    }

    /**
     * Returns {@link Path} to a file where {@code token} realization should be.
     *
     * @param token type token to create implementation for.
     * @param root root directory.
     * @param fileExtension string value of <code>.class</code> or <code>.java</code> depends on what file {@link Path} should be returned.
     * @return {@link Path} to a file where {@code token} realization should be.
     */
    private Path getImplClassPath(Class<?> token, Path root, String fileExtension) {
        return root.resolve(token.getPackageName().replace('.', File.separatorChar))
                .resolve(token.getSimpleName() + ADDITIONAL_IMPL_NAME + fileExtension);
    }

    /**
     * Create -cp for compile.
     * @param token class
     * @return classpath
     * @throws ImplerException if wrong path was given.
     */
    private static String getClassPath(Class<?> token) throws ImplerException {
        try {
            return Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
        } catch (final URISyntaxException e) {
            throw new ImplerException("wrong path is given!");
        }
    }

    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        Path directoryForRealizations = jarFile.getParent();
        implement(token, directoryForRealizations);

        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        Path implJavaPath = getImplClassPath(token, directoryForRealizations, JAVA_EXTENSION);
        String[] args = new String[] {"-encoding", "UTF-8", implJavaPath.toString(), "-cp", getClassPath(token)};
        if (compiler == null || compiler.run(null, null, null, args) != 0) {
            throw new ImplerException("Cannot compile class realization (it is located in temporary directory): " + implJavaPath);
        }

        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        try (final var writer = new JarOutputStream(Files.newOutputStream(jarFile), manifest)) {
            JarEntry entry = new JarEntry(token.getPackageName().replace('.', '/') + "/" + token.getSimpleName() + ADDITIONAL_IMPL_NAME + CLASS_EXTENSION);
            Path implClassPath = getImplClassPath(token, directoryForRealizations, CLASS_EXTENSION);
            writer.putNextEntry(entry);
            Files.copy(implClassPath, writer);
        } catch (final IOException e) {
            throw new ImplerException("Could not write to the output JAR file");
        }
    }

    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        final Constructor<?>[] constructors = token.getDeclaredConstructors();
        if (Modifier.isPrivate(token.getModifiers()) || (!token.isInterface() &&
                (token.isPrimitive() || token.isArray() || Modifier.isFinal(token.getModifiers()) || token.equals(Enum.class)
                || (constructors.length != 0 && Stream.of(constructors).allMatch(c -> Modifier.isPrivate(c.getModifiers())))))) {
            throw new ImplerException("Wrong token");
        } else {
            String className = token.getSimpleName() + ADDITIONAL_IMPL_NAME;
            Path result = getImplClassPath(token, root, JAVA_EXTENSION);

            try {
                if (result.getParent() != null) {
                    Files.createDirectories(result.getParent());
                }
                try (BufferedWriter writer = Files.newBufferedWriter(result, StandardCharsets.UTF_8)) {
                    if (token.isInterface()) {
                        classGenerator(token, IMPLEMENTS, className, token.getCanonicalName(), writer);
                    } else {
                        classGenerator(token, EXTENDS, className, token.getCanonicalName(), writer);
                    }
                } catch (IOException e) {
                    throw new ImplerException("something wrong while writing data to file: \"" + result + "\"", e.getCause());
                }
            } catch (IOException e) {
                throw new ImplerException("something wrong while creating dirs!", e.getCause());
            }
        }
    }

    /**
     * Creates an {@link Implementor} and runs it depending on the arguments provided.
     * <p>
     * If there are two arguments, runs the {@link #implement(Class, Path)} method, converting the provided argument
     * to a class using the {@link Class#forName(String)} method and resolving provided working directory as the path.
     * <p>
     * If there are three arguments and the first one equals to "-jar", runs the {@link #implementJar(Class, Path)} method,
     * converting the second argument to a class using the {@link Class#forName(String)} method and the third argument
     * to a path using the {@link Path#of(String, String...)} method.
     * <p>
     * If the arguments are incorrect or an error occurs during implementation an error message is printed.
     *
     * @param args the provided arguments.
     */
    public static void main(String[] args) {
        if (args == null || args.length < 2 || args.length > 3 || Stream.of(args).anyMatch(Objects::isNull)) {
            System.out.println("The program handle only 2 or 3 command line arguments (arguments are required not to be null).");
            return;
        }

        if (args.length == 3 && !args[0].equals("-jar")) {
            System.out.println("If 3 arguments provided if should be this format: -jar class-name file-name.jar.");
            return;
        }

        JarImpler implementor = new Implementor();
        try {
            if (args.length == 2) {
                implementor.implement(Class.forName(args[0]), Path.of(args[1]));
            } else {
                implementor.implementJar(Class.forName(args[1]), Path.of(args[2]));
            }
        } catch (ImplerException e) {
            System.err.println(e.getMessage() + " " + e.getCause());
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find specified class: " + e.getMessage());
        } catch (InvalidPathException e) {
            System.err.println("Could not convert to path: " + e.getMessage());
        }
    }
}
