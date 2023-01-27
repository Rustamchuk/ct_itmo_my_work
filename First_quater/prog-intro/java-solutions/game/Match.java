package game;

public class Match {
    private final Player player1;
    private final Player player2;
    private final int wins;

    private final int m;
    private final int n;
    private final int k;
    public Match(Player player1, Player player2, int wins, int m, int n, int k) {
        this.player1 = player1;
        this.player2 = player2;
        this.wins = wins;
        this.m = m;
        this.n = n;
        this.k = k;
    }
    public void playMatch() {
        int wins1 = 0;
        int wins2 = 0;
        boolean turn1 = true;

        Game game = new Game(false, player1, player2);
        while (wins1 != wins && wins2 != wins) {
            if (turn1) {
                game.changePlayers(false, player1, player2);
                System.out.println("\nNow turn for Player - 1");
            } else {
                game.changePlayers(false, player2, player1);
                System.out.println("\nNow turn for Player - 2");
            }
            int result;
            result = game.play(new MNKBoard(m, n, k));
            if (!turn1) {
                result = 3 - result;
            }
            System.out.println("Round result: Player - " + (result));
            if (result == 1) {
                wins1++;
            } else if (result == 2) {
                wins2++;
            }
            System.out.println("\nScore: Player 1 -- " + wins1 + "   Player 2 -- " + wins2);
            turn1 = !turn1;
        }
        if (wins1 == wins) {
            System.out.println("\n\nGame result: 1");
        } else {
            System.out.println("Game result: 2");
        }
    }
}
