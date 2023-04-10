package com.daa.sudoku;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


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
        int clauseCount = 0;


        formula.clear();


        // Initialize the FileWriter to store the cnf file
        PrintWriter writer = new PrintWriter("Sudoku Solver/formulas/" + puzzleName + "_formula.cnf");


        // This code implements constraint #8 that ensures that each subgrid in a Sudoku puzzle contains at most one occurrence of each value.
        // Calculate the size of each subgrid
        int subGrid = (int) Math.sqrt(size);
        // Iterate through each subgrid in the Sudoku board
        for(int grid = 1; grid <= size; grid += subGrid) {
            for (int grid2 = 1; grid2 <= size; grid2 += subGrid) {
                // Iterate through each possible value in the subgrid
                for (int val = 1; val <= size; val++) {
                    // Iterate through each cell in the subgrid
                    for (int row = grid; row <= grid + (subGrid - 1); row++) {
                        for (int col = grid2; col <= grid2 + (subGrid - 1); col++) {
                            // Add the literal for the cell and value to the clause
                            formula.add(code(row, col, val, size));
                        }
                    }
                    // Terminate the clause with 0, add it to the formula, and increment the clause count
                    formula.add(0);
                    clauseCount++;
                }
            }
        }






        // This code implements constraint #9 that ensures that given values for cells in a Sudoku puzzle are respected.
        // The code iterates through each row and column of the Sudoku board.
        for (int row = 0; row <= size - 1; row++) { // For each row
            for (int column = 0; column <= size - 1; column++) { // For each column

                // If the current cell value is not equal to 0 (i.e., a given value)
                if (!(board[row][column] == 0)) {
                    // Encode the literal for the given value using the code() function
                    int literal = code(row, column, board[row][column], size);
                    // Add the literal to the formula and terminate the clause with 0
                    formula.add(literal);
                    formula.add(0);
                    // Increment the clause count
                    clauseCount++;
                }
            }
        }



        for (int row = 1; row <= size; row++) { // For each row
            for (int val = 1; val <= size; val++) { // For each value
                for (int col = 1; col <= size; col++) { // For each column in the row
                    formula.add(code(row, col, val, size));
                }
                formula.add(0); // Terminate the clause
                clauseCount++;
            }
        }










        //ROW: no more than once
        for (int i=1;i<size;i++) {
            for (int k=1;k<size; k++) {
                for (int j=1;j<size; j++) {
                    for (int l=j+1;l<size;l++) {
                        formula.add(-1 * code(i,j,k,size));
                        formula.add(-1 * code(i, l, k,size));
                        formula.add(0);
                        clauseCount++;
                    }
                }
            }
        }


        // ROW: at least once
        for (int i = 1; i <= size; i++) {
            for (int k = 1; k <= size; k++) {
                // Create a new clause and add the literals for each cell in the row containing the value k
                ArrayList<Integer> clause = new ArrayList<>();
                for (int j = 1; j <= size; j++) {
                    clause.add(code(i, j, k, size));
                }
                // Terminate the clause with 0 and add it to the formula
                clause.add(0);
                formula.addAll(clause);
                clauseCount++;
            }
        }




        //COLUMN : each shows up at least once
        for (int j=1;j<=size;j++) {
            for (int k=1;k<=size;k++) {
                ArrayList<Integer> clause = new ArrayList<>();
                for (int i=1;i<=size;i++) {
                    clause.add(code(i,j,k,size));
                }
                clause.add(0); // add terminating 0 to the clause
                formula.addAll(clause); // add the literals to the formula
                clauseCount++;
            }
        }




        //COLUMN : no more than once
        for (int j=1;j<=size;j++) {
            for (int k=1;k<=size;k++) {
                for (int i=1;i<=size;i++) {
                    for (int l=i+1;l<=size;l++) {
                        formula.add(-code(i,j,k,size));
                        formula.add(-code(l,j,k,size));
                        formula.add(0);
                        clauseCount++;
                    }
                }
            }
        }







        // Write everything to the file starting with format line
        writer.write("p cnf " + (size*size*size) + " " + clauseCount + "\n");

        for (Integer num : formula) {

            writer.write(num + " ");

            if (num == 0) {
                writer.write("\n"); // print a newline character
            }
        }
        writer.close();
    }



    public int getSize()
    {
        return size;
    }


    // Encode values to base-size
    public int code(int i, int j, int k,int inputSize) {
        return inputSize*inputSize * (i) + inputSize * (j) + k;
    }


    public String decode(int code, int inputSize) {
        int k = code % inputSize;
        int j = (code / inputSize) % inputSize;
        int i = code / (inputSize * inputSize);
        return String.format("(%d, %d, %d)", i , j, k + 1);
    }

}
