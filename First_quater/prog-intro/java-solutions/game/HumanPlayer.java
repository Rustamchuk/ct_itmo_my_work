package game;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class HumanPlayer extends DefaultPlayer implements Player {
    private final PrintStream out;
    private final Scanner in;

    public HumanPlayer(final PrintStream out, final Scanner in) {
        this.out = out;
        this.in = in;
    }

    public HumanPlayer() {
        this(System.out, new Scanner(System.in));
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        while (true) {
            out.println("Position");
            out.println(position);
            out.println(cell + "'s move");
            out.println("Enter row and column");
            int first = 0, second = 0;
            if (checkForNumber("Please enter a number :)"))
                first = in.nextInt() - 1;
            if (checkForNumber("Please enter a second number! >:("))
                second = in.nextInt() - 1;
            final Move move = new Move(first, second, cell);
            if (position.isValid(move)) {
                return move;
            }
            out.println("Move " + move + " is invalid");
        }
    }
    private boolean checkForNumber(String message) {
        while (!in.hasNextInt()) {
            System.out.println(message);
            in.next();
        }
        return true;
    }
}
