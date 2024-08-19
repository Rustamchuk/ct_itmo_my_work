package info.kgeorgiy.ja.nazarov.hello;

import java.util.Arrays;
import java.util.Objects;

/**
 * Utility class for UDP client and server operations.
 */
public class UDPUtils {
    /**
     * Prints a concatenated message from the provided objects.
     *
     * @param messages the objects to print
     */
    public static void printMessage(Object... messages) {
        System.out.println(Arrays.stream(messages)
                .map(Object::toString)
                .reduce("", (acc, message) -> acc + message));
    }

    /**
     * Checks if the provided arguments are valid.
     *
     * @param args the arguments to check
     * @param len  the expected length of the arguments array
     * @return true if the arguments are valid, false otherwise
     */
    public static boolean checkValidArgs(String[] args, int len) {
        return args == null || args.length != len ||
                Arrays.stream(args).anyMatch(Objects::isNull);
    }

    /**
     * Parses an integer argument and checks if it meets the specified barrier.
     *
     * @param arg     the argument to parse
     * @param barrier the minimum acceptable value
     * @param errMsg  the error message to print if the argument is invalid
     * @return the parsed integer
     * @throws IllegalArgumentException if the argument is invalid
     */
    public static int parseIntArg(String arg, int barrier, Object errMsg) {
        int num = Integer.parseInt(arg);
        if (num < barrier) {
            printMessage(errMsg);
            throw new IllegalArgumentException();
        }
        return num;
    }

    /**
     * Parses a string argument and checks if it is non-empty.
     *
     * @param arg    the argument to parse
     * @param errMsg the error message to print if the argument is invalid
     * @return the parsed string
     * @throws IllegalArgumentException if the argument is invalid
     */
    public static String parseStringArg(String arg, Object errMsg) {
        if (arg.isEmpty()) {
            printMessage(errMsg);
            throw new IllegalArgumentException();
        }
        return arg;
    }
}
