package game;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class SequentialPlayer extends DefaultPlayer implements Player {
    @Override
    public Move move(final Position position, final Cell cell) {
//        Board board = (Board) position;
//        board.makeMove()
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                final Move move = new Move(r, c, cell);
                if (position.isValid(move)) {
                    System.out.println("Position");
                    System.out.println(position);
                    System.out.println(cell + "'s move");
                    return move;
                }
            }
        }
        throw new IllegalStateException("No valid moves");
    }
}
