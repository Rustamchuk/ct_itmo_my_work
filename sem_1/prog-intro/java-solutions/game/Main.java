package game;

import java.util.Scanner;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static int m;
    private static int n;
    private static int k;

    public static void main(String[] args) {
        System.out.println("Hello! Let's play! Enter numbers of Lines, Columns, Target, Number fo wins");
        m = (checkForInt("Sorry, but I can get only number for Lines :o"));
        n = (checkForInt("Please write a number for Columns, and let's play! :D"));
        writeValidK(checkForInt("I want only number from you! :~)"));
        int wins = (checkForInt("Please write a number for wins, and let's play! :)"));

        HumanPlayer player1 = new HumanPlayer();
        HumanPlayer player2 = new HumanPlayer();
        player1.setMN(m, n);
        player2.setMN(m, n);
        Match match = new Match(player1, player2, wins, m, n, k);
        match.playMatch();
//        Game game = new Game(false, player1, player2);
//        int result;
//        result = game.play(new MNKBoard(m, n, k));
//        System.out.println("Round result: Player - " + (result));

    }

    public static void writeValidK(int cur) {
        while (true) {
//            if (Math.pow(m, 2) + Math.pow(n, 2) >= cur * cur) {
            if (m >= k || n >= k) {
                k = cur;
                return;
            } else {
                System.out.println("Please write possible target");
                cur = (checkForInt("Please, dear, write a number :|"));
            }
        }
    }

    public static int checkForInt(String message) {
        while (!in.hasNextInt()) {
            System.out.println(message);
            in.nextLine();
        }
        return in.nextInt();
    }
}
