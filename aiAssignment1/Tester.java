package aiAssignment1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
/*
Student: Tony Le
Date: 7/18/2021
Program: Sliding Puzzle Solver
Description:

Enter your sliding puzzle problem with the template stated in the console
(for example, [size] "[initialstate]" [searchmethod] --> 3 " 13425786" BFS).

The size is the dimensions of the square board, Numbers may be between 1-9,
letters can be between A-Z, the initial state must fill in all spots of the square.

Once doing that, the program will test your problem to see if it is solvable.

If solvable, it will use the search method of your choice (BFS, DFS,
GBFS, AStar must be specified) and return two outputs;

In console: A final solution path from initial to goal state.

In ReadMe.txt: The size, initial, and goal state of your problem, also including;
-depth: depth of where the solution of the search tree is found.
-numCreated: number if times a node of the search tree is created.
-numExpanded: how many times the search algorithm acquires the successor state to the current one.
-maxFringe: the max fringe at any point during the search.

 */

public class Tester {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";

    private static void mainMenu() throws IOException {
        int n = 0;
        String initialState = "";
        String searchMethod = "";
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        Scanner console = new Scanner(System.in);

        System.out.print("----------------------\n"+"Sliding Puzzle Solver" +
                "\n----------------------\n[size] \"[initialstate]\" [searchmethod]:");
        String input = console.nextLine();
        System.out.println();

        sb.append("\"");
        boolean endInitialStateRead = false;

        for(int i = 3; i < input.length(); i++) {
            if (endInitialStateRead == true) {
                sb2.append(input.charAt(i));
            }

            if (input.charAt(i) == '"' || input.charAt(i) == '”') {
                sb.append(input.charAt(i));
                endInitialStateRead = true;
                i++;
            }
            if (endInitialStateRead == false) {
                sb.append(input.charAt(i));
            }
        }
        //initial inputs of size, initial state, and search method
        n = Integer.parseInt(String.valueOf(input.charAt(0)));
        initialState = sb.toString();
        searchMethod = sb2.toString();

        System.out.println(ANSI_YELLOW + "YOUR INPUT:\nSize n: " + n +
                "\nInitial State: " + initialState +
                "\nSearch method: " + searchMethod + ANSI_RESET);

        if (checkInputs(n, initialState, searchMethod) == false) {
            throw new IllegalArgumentException(); //checks if inputs are within boundaries.
        } else
            System.out.println(ANSI_GREEN + "\n...Input checking successful.\n" + ANSI_RESET);
        if (checkSolvable(n, initialState) == false) { //checks if the board is solvable.
            System.out.println(ANSI_RED + "...Board is unsolvable.\n" + ANSI_RESET);
            System.out.println("depth = -1\ncreated = -1\nexpanded = -1\nmaxfringe = -1");

            throw new IllegalArgumentException();
        } else
            System.out.println(ANSI_GREEN + "...Board is solvable.\n" + ANSI_RESET);

        playGame(n, initialState, searchMethod);
    }

    private static void playGame(int n, String initialState, String searchMethod) throws IOException {
        String newInitialState;
        String goalState;

        StringBuilder str = new StringBuilder(initialState);
        str.deleteCharAt(initialState.length() - 1); //deletes the parentheses in initial input
        str.deleteCharAt(0);

        newInitialState = str.toString(); //new initialState is created
        goalState = createGoalState(newInitialState); //goalState created

        System.out.println("Goal State: \"" + goalState + "\"\n" );

        Board startBoard = new Board(n, newInitialState); //create start and goal boards
        Board goalBoard = new Board(n, goalState);

        System.out.println("Initial:");
        startBoard.displayGameBoard();

        Solver solve = new Solver(startBoard, goalBoard, searchMethod);
    }

    private static String createGoalState(String newInitialState) {
        String goalState = "";
        String temp;

        StringBuilder str = new StringBuilder(newInitialState);
        str.deleteCharAt(newInitialState.indexOf(' '));
        temp = str.toString();

        char[] goal = temp.toCharArray();
        Arrays.sort(goal);
        for (char ch: goal){
            goalState += ch;
        }
        goalState += ' ';

        return goalState;
    }

    private static int countInversions(String initialState) {
        int numOfInversions = 0;
        System.out.print("Inversions: ");

        for (int i = 1; i < initialState.length() - 1 ; i++) {
            char value = initialState.charAt(i);

            for (int j = i + 1; j < initialState.length() -1; j++) {

                char compareToValue = initialState.charAt(j);

                if ((value > compareToValue) && value != ' ' && compareToValue != ' ') {
                    numOfInversions++;
                    System.out.print(value + "-" +compareToValue + " ");
                }
            }
        }
        System.out.println();
        return numOfInversions;
    }

    private static boolean checkSolvable(int n, String initialState) {
        int numOfInversions = countInversions(initialState);
        System.out.println("Total Inversions: " + numOfInversions + "\n");
        if (n % 2 != 0) { //odd sized boards
            if (numOfInversions % 2 == 0) {
                return true;
            } else return false;
        } else if (n % 2 == 0) { //even sized boards
            int rowWithBlank = (int) (((double) initialState.indexOf(' ') / (double) n));
            int sum = 0;

            if (rowWithBlank == n) {
                rowWithBlank--;
            }
            sum = rowWithBlank + numOfInversions;

            System.out.println("Row with Blank: " + rowWithBlank + "\n");
            if (sum % 2 != 0) {
                return true;
            } else return false;
        }

        throw new IllegalArgumentException();
    }

    private static boolean checkInputs(int n, String initialState, String searchMethod) {
        if (n < 2 || n > 4) {
            System.out.println("\n...Error: Size must be between 2 and 4.\n");
            return false;
        } else if ((initialState.length() - 2) % n != 0) {
            System.out.println("\n...Error: Either too much or not enough inputs in initial state to fill size.\n");
            return false;
        } else if (!searchMethod.equals("DFS") && !searchMethod.equals("GBFS") &&
                !searchMethod.equals("BFS") && !searchMethod.equals("AStar")) {
            System.out.println("\n...Error: Search method must be either: BFS, DFS, GBFS, AStar.\n");
            return false;
        } else
            return true;
    }


    public static void main (String arg[]) {
        while (true) {
            try {
                mainMenu();
            } catch (Exception e) {

            }
        }
    }
}
