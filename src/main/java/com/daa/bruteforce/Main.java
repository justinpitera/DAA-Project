package com.daa.bruteforce;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.*;



/**
 * SAT Bruteforcer
 * Justin Pitera
 * Finalized Date: 2.26.2023
 * Design and Analysis of Algorithms - Spring 2023
 * -----------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * bruteforce.Main.java
 * The bruteforce.Main class serves as the driver class for the Bruteforcer
 * It allows the user to load a single CNF file, solve all CNF files in the formulas' directory, list all files in the formulas directory, or exit the program.
 * It also provides a function to try every possible combination of variables via a bruteforce approach in the formula and determines if there is a satisfying variation.
 */

public class Main
{
    private static final ArrayList<Clause> formula = new ArrayList<Clause>(); // The current formula to solve
    private static int numberOfVariables = 0; // The number of variables per clause in the formula
    private static int numberOfClauses = 0; // The number of clauses per formula file
    private static long totalTime = 0; //The total time in took to solve all the files in the formulas directory
    /**
     * The main method provides a menu for the user to interact with the program.
     * It allows the user to load a single CNF file, solve all CNF files in the formulas' directory, list all files the formulas' directory, or exit the program.
     * @param args the command-line arguments (not used)
     * @throws InterruptedException if the current thread is interrupted while waiting for user input
     * @throws ExecutionException if the computation has a problem
     */

    public static void main(String[] args) throws InterruptedException, ExecutionException
    {
        Scanner scanner = new Scanner(System.in);

        int userChoice = 0;

        while (userChoice != 4)
        {


            File formulasFolderObject = new File(System.getProperty("user.dir") + "/formulas/");
            File[] formulasFileList = formulasFolderObject.listFiles();

            System.out.println("Welcome to the SAT CNF Solver:");
            System.out.println("1. Bruteforce formula");
            System.out.println("2. Bruteforce all formulas");
            System.out.println("3. List all files in formulas directory");
            System.out.println("4. Exit");
            System.out.print("Make a selection (1-4): ");


            userChoice = scanner.nextInt();

            switch (userChoice)
            {
                case 1:
                    System.out.print("Enter file name: ");
                    String fileName = scanner.next();
                    File formulaFile = new File(System.getProperty("user.dir") + "/formulas/"+ fileName);
                    loadFile(formulaFile);
                    // print the working path for the user to see
                    System.out.println("Working on: " + fileName);
                    // Display general information about current formula
                    System.out.println("Number of Variables: " + numberOfVariables + ", Number of Clauses: " + formula.size());
                    System.out.println("Attempting to bruteforce...");
                    // Attempt to bruteforce current formula
                    bruteForce();
                    System.out.println("------------------------------------------------------------------------------------------------------------");
                    break;
                case 2:
                    System.out.println("Attempting to solve all formulas in formulas directory...");
                    for (File file : formulasFileList)
                    {
                        // Loads file into formula array list
                        loadFile(file);
                        // print the working path for the user to see
                        System.out.println("Working on: " + file.getPath());
                        // Display general information about current formula
                        System.out.println("Number of Variables: " + numberOfVariables + ", Number of Clauses: " + formula.size());
                        System.out.println("Attempting to bruteforce...");
                        // Attempt to bruteforce current formula
                        bruteForce();
                        System.out.println("------------------------------------------------------------------------------------------------------------");
                    }
                    System.out.println(formulasFileList.length + " files read and solved in " + totalTime + " milliseconds.");
                    break;
                case 3:
                    System.out.println("All files in formulas directory:");
                    for (File file : formulasFileList)
                    {
                        System.out.println(file.getName() + " ... " + file.getPath());
                    }
                    break;
                case 4:
                    System.out.println("Closing...");
                    break;
                default:
                    System.out.println("Invalid choice, try again.");
                    break;
            }
        }
    }



    /**
     * The loadFile function takes a DIMACS CNF file, parses it into a list of clauses, and puts them into a formula.
     * It also prints the clauses, variables, and maximum number of variations generated by 2^n where n is the number of variables.
     * @param cnfFile the formula file to be solved
     * @throws InterruptedException if the current thread is interrupted while waiting for user input
     * @throws ExecutionException if the computation has a problem
     */
    public static void loadFile(File cnfFile) throws InterruptedException, ExecutionException
    {
        formula.clear(); // Clears the list from the last one
        try
        {
            // Grab the file from the parameters
            Scanner scanner = new Scanner(cnfFile);
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                // Comment line, skip
                if (line.startsWith("c"))
                {
                    continue;
                }

                // Format line, [n] [m]
                // n = Number of variables
                // m = Number of clauses in file
                if (line.startsWith("p"))
                {
                    /*
                    The trim() method removes any leading or trailing whitespace characters from the input string.
                     The split("\\s+") method splits the string into an array of substrings based on one or more whitespace characters.
                     */
                    String[] args = line.trim().split("\\s+");
                    numberOfVariables = Integer.parseInt(args[2]); //variables
                    // How many clauses are present in the formula.
                    numberOfClauses = Integer.parseInt(args[3]); //clauses
                    for (int i = 0; i < numberOfClauses; i++)
                    {
                        // Base case no more integers to be read
                        if (!(scanner.hasNextInt()))
                        {
                            break;
                        }

                        // Initialize new clause
                        Clause newClause = new Clause(new LinkedList<Integer>());

                        // Initialize current variable being read
                        int currentVariable = scanner.nextInt();

                        // While the current variable being read isn't a 0, add it to the new clause and move the currentVariable to the next variable.
                        while (currentVariable != 0)
                        {
                            newClause.addVariable(currentVariable);
                            currentVariable = scanner.nextInt();
                        }
                        //Add clause to the formula
                        formula.add(newClause);
                    }
                }
            }
        } catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }



    /**
     * Tries every possible combination of variables in the formula and determines if there is a satisfying variation.
     * This method initializes the first variation to be all false, and then tests each variation in sequence using binary addition
     * to generate each variation. The isSatisfied method is then used to check if the current variation satisfies the formula. It uses getNextVariation
     * method to generate the next variation to be tested until it reaches the final variation or finds a satisfying solution.
     * If it finds a satisfying solution, it prints the solution and returns true. If it goes through all possible
     * variations and none satisfy the formula, it returns false.
     *
     * @throws IllegalArgumentException if formula is null
     */
    public static void bruteForce ()
    {
        long startTime = System.currentTimeMillis();
        boolean formulaSatisfied = false;

        //initialize first variation, all false. but as this goes through the for loop, the final variation should be all true
        int[] currentVariation = new int[numberOfVariables];

        //Test the first variation against the list of clauses
        formulaSatisfied = isSatisfied(currentVariation);

        while (!isFinalVariation(currentVariation) && !formulaSatisfied)
        {
            currentVariation = getNextVariation(currentVariation);
            formulaSatisfied = isSatisfied(currentVariation);
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        totalTime = totalTime + elapsedTime;
        if (formulaSatisfied)
        {
            System.out.println("Satisfiable: Yes");
            // Print out variation that satisfies the formula
            System.out.print("Solution: ");
            System.out.print("[ ");
            for (int variable : currentVariation)
            {
                System.out.print(variable + " ");
            }
            System.out.println("]");
        } else
        {
            System.out.println("Satisfiable: No");
        }
        formulaSatisfied = false;
        System.out.println("Elapsed time: " + elapsedTime + " milliseconds");
    }




    /**
     * Determines if the input integer array satisfies the boolean formula.
     *
     * The method checks whether the input array satisfies the boolean formula represented as a list of clauses.
     * Each clause is represented as a list of literals (positive or negative integers) that are OR-ed together.
     * The method checks if at least one literal in each clause is satisfied by the current variation.
     * If all clauses are satisfied, the method will return true indicating that the input array satisfies the formula.
     * Otherwise, if at least one clause is not satisfied, the method will return false indicating that the input array
     * does not satisfy the formula.
     *
     * @param currentVariation the input integer array to be checked for satisfying the boolean formula
     * @return true if the input array satisfies the boolean formula; false otherwise
     */
    private static boolean isSatisfied(int[] currentVariation)
    {
        // Initialize as unsatisfied
        boolean formulaSatisfied = false;

        // Go through each clause in the formula
        for (Clause currentClause : formula)
        {
            boolean clauseSatisfied = false;
            // Go through each literal in the clause
            for (int literal : currentClause.getVariables())
            {
                int state = 0; // initialize as off
                if (literal > 0)
                {
                    state = 1; //on if the literal is more than zero
                }
                // Check if the literal is satisfied by the current variation
                if (currentVariation[Math.abs(literal) - 1] == state)
                {
                    clauseSatisfied = true;
                    break;
                }
            }

            // If the clause is not satisfied, try the next variation
            if (!clauseSatisfied)
            {
                formulaSatisfied = false;
                break;
            }
            else
            {
                formulaSatisfied = true;
            }
        }
        return formulaSatisfied;
    }

    /**
     * Determines if the input integer array represents the final variation.
     *
     * Checks if all elements in input array are equal to 1 or not.
     * If there exists any element in the input array that is not equal to 1, the method will return false
     * indicating that the input array is not the final variation. Otherwise, if all the elements in the
     * input array are equal to 1, the method will return true indicating that the input array is the final
     * variation.
     *
     * @param currentVariation the input integer array to be checked for being the final variation
     * @return true if the input array is the final variation, false otherwise
     */
    public static boolean isFinalVariation(int[] currentVariation)
    {
        for (int i = 0; i < currentVariation.length; i++)
        {
            if (currentVariation[i] != 1)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the next binary variation
     *
     * @param vars an array representing the current binary variation
     * @return an array representing the next binary variation in lexicographic order
     */
    public static int[] getNextVariation(int[] vars)
    {
        // Initialize a next variation by creating a copy of the current variation.
        int[] nextVariation = Arrays.copyOf(vars, vars.length);
        int index = nextVariation.length - 1;
        // Find the rightmost 0 value
        while (index >= 0 && nextVariation[index] == 1)
        {
            index--;
        }
        if (index >= 0)
        {
            // Flip the rightmost 0 value to 1
            nextVariation[index] = 1;
            // Change all 1 values to the right of the rightmost 0 to 0
            for (int i = index + 1; i < nextVariation.length; i++)
            {
                nextVariation[i] = 0;
            }
        }
        return nextVariation;
    }
}