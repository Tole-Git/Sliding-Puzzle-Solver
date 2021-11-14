package aiAssignment1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Solver {
    private Board startBoard;
    private Board goalBoard;
    private long expanded;
    private long created;
    private long maxFringe;

    public Solver(Board startBoard, Board goalBoard, String searchMethod) throws IOException {
        this.startBoard= startBoard;
        this.goalBoard = goalBoard;
        expanded = 0;
        created = 0;
        maxFringe = 0;

        if (searchMethod.equals("BFS")) {
            solveBFS();
        } else if (searchMethod.equals("DFS")) {
            solveDFS();
        } else if (searchMethod.equals("GBFS")) {
            BoardH startBoardH = new BoardH(startBoard.getSize()
                    , startBoard.getCurrentStateToString(), goalBoard);
            BoardH goalBoardH = new BoardH(goalBoard.getSize()
                    , goalBoard.getCurrentStateToString(), goalBoard);
            solveGBFS(startBoardH, goalBoardH);
        } else if (searchMethod.equals("AStar")) {
            solveAStar();
        } else throw new IllegalArgumentException();
    }

    private void solveAStar() {

    }

    private void solveGBFS(BoardH startBoardH, BoardH goalBoardH) throws IOException {
        System.out.println("hello");
        BoardH currentBoard = new BoardH(startBoardH);
        LinkedList<BoardH> visited = new LinkedList<>();
        PriorityQueue<BoardH> unvisited = new PriorityQueue<>();

        while (currentBoard != null && !currentBoard.equals(goalBoardH)) {
            currentBoard.displayGameBoard();
            if (!visited.contains(currentBoard)) {
                for (Direction d : Direction.values()) {
                    unvisited.add(new BoardH(currentBoard, d));
                    created++;
                }
                visited.add(new BoardH(currentBoard));
                expanded++;

                if (unvisited.size() > maxFringe) {
                    maxFringe = unvisited.size();
                }
            }
            currentBoard = unvisited.poll();
        }
        printSolution(currentBoard);
    }

    private void solveDFS() throws IOException {
        Board currentBoard = new Board(startBoard);
        HashSet<Board> visited = new HashSet<Board>();
        ArrayDeque<Board> unvisited = new ArrayDeque<Board>();

        while (currentBoard != null && !currentBoard.equals(goalBoard)) {
            if (!visited.contains(currentBoard)) {
                for (Direction d : Direction.values()) {
                    unvisited.push(new Board(currentBoard, d));
                    created++;
                }
                visited.add(new Board(currentBoard));
                expanded++;

                if (unvisited.size() > maxFringe) {
                    maxFringe = unvisited.size();
                }
            }
            currentBoard = unvisited.pop();
        }

        printSolution(currentBoard);
    }

    private void solveBFS() throws IOException {
        Board currentBoard = new Board(startBoard);
        ArrayList<Board> visited = new ArrayList<>();
        LinkedBlockingQueue<Board> unvisited = new LinkedBlockingQueue<Board>();

        while (currentBoard != null && !currentBoard.equals(goalBoard)) {
            if (!visited.contains(currentBoard)) {
                for (Direction d : Direction.values()) {
                    unvisited.add(new Board(currentBoard, d));
                    created++;
                }
                visited.add(new Board(currentBoard));
                expanded++;

                if (unvisited.size() > maxFringe) {
                    maxFringe = unvisited.size();
                }
            }
            currentBoard = new Board(unvisited.poll());
        }
        printSolution(currentBoard);
    }

    public void printSolution(Board c) throws IOException {
        if (c == null) {
            System.out.println("...Could not find solution.");
            throw new IllegalArgumentException();
        }

        ArrayDeque<Board> stack = new ArrayDeque<>();
        ArrayList<Board> solution = new ArrayList<>();
        stack.push(c);
        writeReadMe(c);

        System.out.println("...Solution found.\n\nExpanded: " + expanded +
                "\nDepth: " + c.getDepth()
                +"\nCreated: " + created
                +"\nmaxFringe: " + maxFringe
                + "\n\n...Please hold while solution loads.");

        while (stack.peek().getParent() != null) {
            stack.push(stack.peek().getParent());
        }
        while (!stack.isEmpty()) {
            if (!solution.contains(stack.peek())){
                solution.add(stack.pop());
            } else stack.pop();
        }
        for (Board b : solution) {
            b.displayGameBoard();
            System.out.println("-----------");
        }
    }

    public void printSolution(BoardH c) throws IOException {
        if (c == null) {
            System.out.println("...Could not find solution.");
            throw new IllegalArgumentException();
        }

        ArrayDeque<BoardH> stack = new ArrayDeque<>();
        ArrayList<BoardH> solution = new ArrayList<>();
        stack.push(c);
        writeReadMe(c);

        System.out.println("...Solution found.\n\nExpanded: " + expanded +
                "\nDepth: " + c.getDepth()
                +"\nCreated: " + created
                +"\nmaxFringe: " + maxFringe
                + "\n\n...Please hold while solution loads.");

        while (stack.peek().getParent() != null) {
            stack.push(stack.peek().getParent());
        }
        while (!stack.isEmpty()) {
            if (!solution.contains(stack.peek())){
                solution.add(stack.pop());
            } else stack.pop();
        }
        for (BoardH b : solution) {
            b.displayGameBoard();
            System.out.println("-----------");
        }
    }

    public void writeReadMe(Board c) throws IOException {
        FileWriter myWriter = new FileWriter("src\\aiAssignment1\\ReadMe.txt");
        myWriter.write("\nSize: " + c.getSize()
                +"\nInitial " + c.getCurrentStateToString()
                +"\nGoal: " + goalBoard.getCurrentStateToString()
                +"\n\nExpanded: " + expanded +
                "\nDepth: " + c.getDepth()
                +"\nCreated: " + created
                +"\nmaxFringe: " + maxFringe);
        myWriter.close();
        System.out.println("...Successfully wrote to ReadMe.txt.\n");
    }

    public void writeReadMe(BoardH c) throws IOException {
        FileWriter myWriter = new FileWriter("src\\aiAssignment1\\ReadMe.txt");
        myWriter.write("Size: " + c.getSize()
                +"\nInitial " + c.getCurrentStateToString()
                +"\nGoal: " + goalBoard.getCurrentStateToString()
                +"\n\nExpanded: " + expanded
                + "\nDepth: " + c.getDepth()
                +"\nCreated: " + created
                +"\nmaxFringe: " + maxFringe);
        myWriter.close();
        System.out.println("...Successfully wrote to ReadMe.txt.\n");
    }
}
