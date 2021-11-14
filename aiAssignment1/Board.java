package aiAssignment1;

import java.util.Arrays;
import java.util.Objects;

public class Board {
    private int size;
    private String initialState;
    private char[][] gameBoard;
    private int emptyX, emptyY;
    private long depth;
    private Board parent;

    public Board (Board board) { //copy
        this.size = board.size;
        this.initialState = board.initialState;
        createGameBoard();
        findPosition();
        this.depth = board.depth;
        this.parent = board;
    }

    public Board(Board board, Direction d) { //copy and move
        this.size = board.size;
        this.initialState = board.initialState;
        createGameBoard();
        findPosition();
        this.depth = board.depth;
        this.parent = board;
        move(d);
        this.depth++;
    }

    public Board(int size, String initialState) { //initialize board
        this.size = size;
        this.initialState = initialState;
        createGameBoard();
        findPosition();
        this.depth = 0;
        this.parent = null;
    }

    private void createGameBoard() {
        gameBoard = new char[size][size];
        int initialStateCount = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                gameBoard[i][j] = initialState.charAt(initialStateCount);
                initialStateCount++;
            }
        }
    }

    private void findPosition() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (gameBoard[i][j] == ' ') {
                    emptyY = i;
                    emptyX = j;
                }
            }
        }
    }

    public void displayGameBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(gameBoard[i][j] + "   ");
            }
            System.out.println("\n");
        }
    }

    public boolean move(Direction d) {
        switch (d) {
            case UP:
                if (emptyY > 0) {
                    gameBoard[emptyY][emptyX] = gameBoard[emptyY - 1][emptyX];
                    emptyY--;
                    gameBoard[emptyY][emptyX] = ' ';
                    updateState();
                    return true;
                }else return false;
            case DOWN:
                if (emptyY < (size-1)) {
                    gameBoard[emptyY][emptyX] = gameBoard[emptyY + 1][emptyX];
                    emptyY++;
                    gameBoard[emptyY][emptyX] = ' ';
                    updateState();
                    return true;
                } else return false;
            case LEFT:
                if (emptyX > 0) {
                    gameBoard[emptyY][emptyX] = gameBoard[emptyY][emptyX - 1];
                    emptyX--;
                    gameBoard[emptyY][emptyX] = ' ';
                    updateState();
                    return true;
                }else return false;
            case RIGHT:
                if (emptyX < (size-1)) {
                    gameBoard[emptyY][emptyX] = gameBoard[emptyY][emptyX + 1];
                    emptyX++;
                    gameBoard[emptyY][emptyX] = ' ';
                    updateState();
                    return true;
                }else return false;
        }
        return false;
    }

    private void updateState() {
        initialState = getCurrentStateToString();
    }

    public String getCurrentStateToString() {
        String currentState = "";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                currentState += gameBoard[i][j];
            }
        }
        return currentState;
    }

    public long getDepth() {
        return depth;
    }

    public Board getParent() {
        return parent;
    }

    public int getSize() {
        return size;
    }

    public char[][] getGameBoard() {
        return gameBoard;
    }

    @Override
    public boolean equals (Object o) {
        if (o == null) {
            return false;
        }
        Board other = (Board) o;
        if (hashCode() == other.hashCode()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getCurrentStateToString().hashCode();
        return result;
    }
}
