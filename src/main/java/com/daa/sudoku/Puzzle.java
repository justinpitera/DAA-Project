package com.daa.sudoku;

import java.io.*;
import java.util.ArrayList;


public class Puzzle {
    private int[][] board;
    private String filePath;
    private String puzzleName;
    private int size;

    private static final ArrayList<Integer> formula = new ArrayList<>(); // The current formula to generate a file for

    /**
     * Puzzle
     * @param filePath Where to read the puzzle (file path)
     * @throws IOException Indicates a failure in IO operations
     */
    public Puzzle(String filePath) throws IOException {
        this.board = readBoardFile(filePath);
        this.filePath = filePath;

        // Initialize file to get puzzle's name
        File puzzleFile = new File(filePath);
        this.puzzleName = puzzleFile.getName();

    }

    /**
     * The readBoardFile method reads a Sudoku puzzle file and stores the puzzle in a 2-Dimensional Array of integers
     * @param filePath Where to read the puzzle (file path)
     * @throws IOException Indicates a failure in IO operations
     */
    public int[][] readBoardFile(String filePath) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        size = Integer.parseInt(reader.readLine().trim());
        size = (int) Math.pow(size,2);
        reader.readLine();
        int[][] board = new int[size][size];

        for (int i = 0; i < size; i++) {
            String[] line = reader.readLine().trim().split(" ");
            for (int j = 0; j < size; j++) {
                board[i][j] = Integer.parseInt(line[j]);
            }
        }
        reader.close();
        return board;
    }
    public void printBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }

    }

    /**
     * The generateCNFFile method generates a DIMACS CNF file based on the constraints of Sudoku and the clues given in a loaded puzzle.
     *
     * Saves to Application Startup Path\\Sudoku Solver\\formulas\\[puzzle_name]_formula.cnf
     * @throws IOException Indicates a failure in IO operations
     */
    public void generateCNFFile() throws IOException {
        formula.clear();


        // Initialize the FileWriter to store the cnf file
        PrintWriter writer = new PrintWriter("Sudoku Solver/formulas/" + puzzleName + "_formula.cnf");

        // Add DIMACS format line
        int numberOfVariables = ((size*size) * size);
        // Concern for later: how am i going to get this at the top of the file?
        int numberOfClauses = 0;

        // Constraint # 1 - Each cell in the sizeXsize puzzle must have a value between 1 and size
        for (int row = 1; row <= size - 1; row++) { // For each row
            for (int column = 1; column <= size - 1; column++) { // For each column per row
                for (int cell = 1; cell <= size - 1; cell++) { // For each cell
                    formula.add(code(row,column,cell,size));
                }
                formula.add(0);
            }
        }





        // Final Constraint: given clues
        for (int row = 0; row <= size - 1; row++) { // For each row
            for (int column = 0; column <= size - 1; column++) { // For each column
                if (!(board[row][column] == 0)) { // If the current value [i,j] ! = 0
                    int literal = code(row, column, board[row][column],size) - 1; // Encode the literal
                    formula.add(literal);
                    formula.add(0);
                }
            }
        }
        for (Integer num : formula) {
            writer.print(num + " ");
            if (num == 0) {
                writer.println(); // print a newline character
            }
        }
        writer.close();







    }




    // Encode values to base-size
    public int code(int i, int j, int k,int size) {
        return (size * size * i + size * j + k);
    }


    public String decode(int code, int size) {
        int k = code % size;
        int j = (code / size) % size;
        int i = code / (size * size);
        return String.format("(%d, %d, %d)", i , j, k + 1);
    }




}







