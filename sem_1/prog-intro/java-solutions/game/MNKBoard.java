package game;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class MNKBoard implements Board, Position {
    private final int target;
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.E, '.'
    );

    private final Cell[][] cells;
    private Cell turn;

    public MNKBoard(int m, int n, int k) {
        this.cells = new Cell[m][n];
        target = k;
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
    }

    @Override
    public Position getPosition() {
        return this;
    }

    @Override
    public Cell getCell() {
        return turn;
    }

    @Override
    public Result makeMove(final Move move) {
        if (!isValid(move)) {
            return Result.LOSE;
        }

        cells[move.getRow()][move.getColumn()] = move.getValue();

        int empty = 0;
        int inDiag1 = 0;
        int inDiag2 = 0;
        for (int u = 0; u < cells.length; u++) {
            int inRow = 0;
            int inColumn = 0;
            loop2:
            for (int v = 0; v < cells[u].length; v++) {
                inDiag1 = 0;
                inDiag2 = 0;
                if (cells[u][v] == turn) {
                    inRow++;
                } else if (inRow < target) {
                    inRow = 0;
                } else {
                    break;
                }

                if (v < cells.length && u < cells[0].length) {
                    if (cells[v][u] == turn) {
                        inColumn++;
                    } else if (inColumn < target) {
                        inColumn = 0;
                    } else {
                        break;
                    }
                }

                if (cells[u][v] == Cell.E) {
                    empty++;
                }

                for (int i = 0; i < target; i++) {
                    if (u + i < cells.length) {
                        if (v + i < cells[0].length) {
                            if (cells[u + i][v + i] == turn) {
                                inDiag1++;
                            } else if (inDiag1 < target) {
                                inDiag1 = 0;
                            } else {
                                break loop2;
                            }
                        }
                        if (v - i >= 0) {
                            if (cells[u + i][v - i] == turn) {
                                inDiag2++;
                            } else if (inDiag2 != target) {
                                inDiag2 = 0;
                            } else {
                                break loop2;
                            }
                        }
                    }
                }
            }
            if (
                    inRow == target || inColumn == target ||
                            inDiag1 == target || inDiag2 == target
            ) {
                System.out.println(toString());
                return Result.WIN;
            }
        }
        if (empty == 0) {
            return Result.DRAW;
        }

        turn = turn == Cell.X ? Cell.O : Cell.X;
        return Result.UNKNOWN;
    }

    @Override
    public void ClearBoard() {
        for (Cell[] cell : cells) {
            Arrays.fill(cell, Cell.E);
        }
    }

    @Override
    public boolean isValid(final Move move) {
        return 0 <= move.getRow() && move.getRow() < cells.length
                && 0 <= move.getColumn() && move.getColumn() < cells[0].length
                && cells[move.getRow()][move.getColumn()] == Cell.E
                && turn == getCell();
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("   ");
        for (int l = 0; l < cells[0].length; l++) {
            if (l < 9) {
                sb.append(" ").append(l + 1);
            } else {
                sb.append(l + 1);
            }
            sb.append("|");
        }
        for (int r = 0; r < cells.length; r++) {
            sb.append("\n");
            if (r < 9) {
                sb.append(" ").append(r + 1).append("|");
            } else {
                sb.append(r + 1).append("|");
            }
            for (int c = 0; c < cells[r].length; c++) {
                sb.append(" ").append(SYMBOLS.get(cells[r][c])).append("|");
            }
        }
        return sb.toString();
    }
}
