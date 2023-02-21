import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;

import java.util.concurrent.*;
// To Do:
//        (1) Add bruteforce functionality
//        (2) Add code to test how many ms it takes to bruteforce
//        Remaining two of these tasks being completed would result in Mission complete.

public class Main
{

    private static ArrayList<Clause[]> formula = new ArrayList<Clause[]>();
    private static int numClauses = 0; // How many clauses are present in the formula.
    private static int numVars = 0; // The number of variables that can be in a clause at any given time throughout the formula

    public static void main(String[] args) throws InterruptedException, ExecutionException
    {
        // Test code
        File testFolderObject = new File(System.getProperty("user.dir") + "/Tests/");
        File[] testFileList = testFolderObject.listFiles();

        for (File file : testFileList)
        {
            loadFile(file);
        }

        System.out.println(testFileList.length + " files read and solved.");

    }


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
                if (line.startsWith("c"))
                {
                    continue;
                }

                if (line.startsWith("p")) {

                    String[] args = line.split(" ");
                    numVars = Integer.parseInt(args[2]); //variables
                    numClauses = Integer.parseInt(args[3]); //clauses

                    for (int i = 0; i < numClauses; i++)
                    {
                        //initialize a new clause to store the variables
                        Clause newClause = new Clause(numVars, numClauses, new LinkedList<Integer>());
                        if (scanner.hasNextLine())
                        {
                            String[] varStrings = scanner.nextLine().trim().split(" ");
                            for (String varString : varStrings)
                            {
                                int var = Integer.parseInt(varString);
                                if (var != 0)
                                {
                                    newClause.addVariable(var);
                                }
                            }
                            // Add clause to formula
                            formula.add(new Clause[]{newClause});
                        }
                    }
                }
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        // print the list for the user to see

        System.out.println("Working on: " + cnfFile.getPath());
        BigInteger maxVariations = BigInteger.valueOf(2).pow(numVars);
        System.out.println("Number of Variables: " + numVars + ", Number of Clauses: " + formula.size() + ", Maxiumum Variations: " + maxVariations.toString());
        System.out.println("Attempting to bruteforce...");
        bruteForce();
        System.out.println("------------------------------------");
    }



/*
function brute_force_solve(variables, clauses):
    for assignment in all_possible_assignments(variables):
        if satisfies(assignment, clauses):
            return assignment
    return none
 */

    public static void bruteForce()
    {
        long startTime = System.currentTimeMillis();

        //initialize first variation, all false. but as this goes through the for loop, the final variation should be all true
        int[] currentVariation = new int[numVars];

        // Generate 2^(numVars) number of variations
        BigInteger maxVariations = BigInteger.valueOf(2).pow(numVars);
        for (BigInteger i = BigInteger.ZERO; i.compareTo(maxVariations) < 0; i = i.add(BigInteger.ONE))
        {
            // Check ifSatisfiable here but how... check the notes in Clause.java i made. show it to the tutor ask if its right

            currentVariation = getNextVariation(currentVariation);
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Satisfiable: null" ); // This will eventually be implemented
        System.out.println("Elapsed time: " + elapsedTime + " milliseconds"); // Check, is it supposed to be millis?
    }



    // This method is based off of binary addition to create all the variations for n level of variations.
    // This comes out to 2^n level of variations.
    public static int[] getNextVariation(int[] vars) {
        int[] nextVariation = Arrays.copyOf(vars, vars.length);
        int index = nextVariation.length - 1;
        // Find the rightmost 0 value
        while (index >= 0 && nextVariation[index] == 1) {
            index--;
        }
        if (index >= 0) {
            // Flip the rightmost 0 value to 1
            nextVariation[index] = 1;
            // Change all 1 values to the right of the rightmost 0 to 0
            for (int i = index + 1; i < nextVariation.length; i++) {
                nextVariation[i] = 0;
            }
        }
        return nextVariation;
    }











}




