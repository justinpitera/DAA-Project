package com.daa.sudoku;

import java.io.*;


public class Puzzle {
    private int[][] board;
    private String filePath;
    private String puzzleName;
    private int size;



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


        // Initialize the formula, the list of clauses
        StringBuilder formula = new StringBuilder();
        // Initialize the FileWriter to store the cnf file
        FileWriter writer = new FileWriter("Sudoku Solver/formulas/" + puzzleName + "_formula.cnf");

        // Add DIMACS format line
        int numberOfVariables = ((size*size) * size);
        // Concern for later: how am i going to get this at the top of the file?
        int numberOfClauses = 0;
        formula.append("p cnf " + numberOfVariables + " " + numberOfClauses + "\n");

        // Constraint # 1 - Each cell in the sizeXsize puzzle must have a value between 1 and size
        for (int i = 1; i <= size - 1; i++) { // For each row
            for (int j = 1; j <= size - 1; j++) { // For each column per row
                for (int k = 1; k <= size - 1; k++) { // For each cell
                    formula.append(code(i, j, k, size) + " ");
                }
                formula.append("0\n");
            }
        }


        // Constraint # 2 - Each Column must containt a value between 1 and 9 exactly once
        // Constraint # 1 - Each cell in the sizeXsize puzzle must have a value between 1 and size
        for (int i = 1; i <= size - 1; i++) { // For each row
            for (int j = 1; j <= size - 1; j++) { // For each column per row
                for (int k = 1; k <= size - 1; k++) { // For each cell
                }
                formula.append("0\n");
            }
        }




        // Final Constraint: given clues
        for (int i = 0; i <= size - 1; i++) { // For each row
            for (int j = 0; j <= size - 1; j++) { // For each column
                if (!(board[i][j] == 0)) { // If the current value [i,j] ! = 0
                    int literal = code(i, j, board[i][j],size) - 1; // Encode the literal
                    formula.append(literal).append(" 0\n"); // Append encoded literal
                }
            }
        }

        // Print out the generated formula to the user
        System.out.println("Generated formula for "  + puzzleName + ":");
        System.out.println(formula.toString());


        System.out.println(size);
        // Write to file and close file
        writer.write(formula.toString());
        writer.close();
        System.out.println(decode(2273,9));
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







