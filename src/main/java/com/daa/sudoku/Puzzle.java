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



        // Constraint #1
        // ROW: A value appears at least once
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

        // Constraint #2
        //ROW: A value appears at most once
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


        // Constraint #3
        //COLUMN : each value appears at least once
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


        // Constraint #4
        //COLUMN : each value appears at most once
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



        // Constraint #5
        // SUBGRID: at least once
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


        // Constraint #6
        // SUBGRID: at most once
        // Iterate through each subgrid in the Sudoku board
        for(int grid = 1; grid <= size; grid += subGrid) {
            for (int grid2 = 1; grid2 <= size; grid2 += subGrid) {
                // Iterate through each possible value in the subgrid
                for (int val = 1; val <= size; val++) {
                    // Iterate through each cell in the subgrid
                    for (int row = grid; row <= grid + (subGrid - 1); row++) {
                        for (int col = grid2; col <= grid2 + (subGrid - 1); col++) {
                            // Iterate through each other cell in the subgrid
                            for (int row2 = row; row2 <= grid + (subGrid - 1); row2++) {
                                for (int col2 = col + 1; col2 <= grid2 + (subGrid - 1); col2++) {
                                    // Add a clause that ensures that val cannot appear in both cells
                                    formula.add(-code(row, col, val, size));
                                    formula.add(-code(row2, col2, val, size));
                                    formula.add(0);

                                    clauseCount++;
                                }
                            }
                        }
                    }
                }
            }
        }



        // Constraint #7
        // CELL: at least one value in each cell
        for (int row = 1; row <= size; row++) {
            for (int col = 1; col <= size; col++) {
                ArrayList<Integer> clause = new ArrayList<>();
                for (int val = 1; val <= size; val++) {
                    clause.add(code(row, col, val, size));
                }
                clause.add(0); // Terminate the clause
                formula.addAll(clause);
                clauseCount++;
            }
        }


        // Constraint #8
        // CELL: at most one value in each cell
        for (int row = 1; row <= size; row++) {
            for (int col = 1; col <= size; col++) {
                for (int val1 = 1; val1 <= size; val1++) {
                    for (int val2 = val1 + 1; val2 <= size; val2++) {
                        int literal1 = code(row, col, val1, size);
                        int literal2 = code(row, col, val2, size);
                        formula.add(-literal1);
                        formula.add(-literal2);
                        formula.add(0);
                        clauseCount++;
                    }
                }
            }
        }




        // Constraint #9
        // Givens: Ensures that the given values in the sudoku puzzle are respected
        // iterates through each row and column of the Sudoku board.
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
