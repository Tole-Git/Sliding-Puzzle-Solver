package aiAssignment1;

public class BoardH implements Comparable<BoardH>{
    private int size;
    private String initialState;
    private char[][] gameBoard;
    private int emptyX, emptyY;
    private long depth;
    private BoardH parent;
    private Board goalState;
    private int heuristic;

    public BoardH(BoardH board) { //copy
        this.size = board.size;
        this.initialState = board.initialState;
        createGameBoard();
        findPosition();
        this.depth = board.depth;
        this.parent = board;
        this.goalState = board.goalState;
        this.heuristic = board.heuristic;
    }

    public BoardH(BoardH board, Direction d) { //copy and move
        this.size = board.size;
        this.initialState = board.initialState;
        createGameBoard();
        findPosition();
        this.depth = board.depth;
        this.parent = board;
        this.goalState = board.goalState;
        move(d);
        this.depth++;
        findHeuristic();
    }

    public BoardH(int size, String initialState, Board goalState) { //initialize board H
        this.size = size;
        this.initialState = initialState;
        createGameBoard();
        findPosition();
        this.depth = 0;
        this.goalState = goalState;
        this.parent = null;
        findHeuristic();
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

    public BoardH getParent() {
        return parent;
    }

    @Override
    public boolean equals (Object o) {
        if (o == null) {
            return false;
        }
        BoardH other = (BoardH) o;
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

    @Override
    public int compareTo(BoardH o) {
        if (heuristic > o.getHeuristic()) {
            return 1;
        } else if (heuristic < o.getHeuristic()) {
            return -1;
        }
        return 0;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public int findHeuristic() {
        int misplacedTiles = 0;
        int sumTilesMan = 0;
        int total = 0;
        char[][] goalGameBoard = goalState.getGameBoard();
        String goalStateString = goalState.getCurrentStateToString();

        for (int i = 0; i < goalStateString.length(); i++) {
            if (initialState.charAt(i) != goalStateString.charAt(i)) {
                misplacedTiles++;
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                char currentValue = gameBoard[i][j];
                for (int k = 0; k < size; k++) {
                    for (int l = 0; l < size; l++) {
                        if (goalGameBoard[k][l] == currentValue) {
                            sumTilesMan += (l - j) + (k - i);
                        }
                    }
                }
            }
        }
        total = misplacedTiles + sumTilesMan;
        return total;
    }

    public int getSize() {
        return size;
    }
}
