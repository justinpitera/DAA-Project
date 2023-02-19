import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import java.util.concurrent.*;
// To Do:
//        (1) Add bruteforce functionality
//        (2) Add code to test how many ms it takes to bruteforce
//        Remaining two of these tasks being completed would result in Mission complete.

public class Main {





    static ArrayList<Clause[]> formula = new ArrayList<Clause[]>();
    static int numClauses = 0; // How many clauses are present in the formula.
    static int maxNumVars = 0; // The maximum number of variables that can be in a clause at any given time throughout the formula

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


    public static void loadFile(File cnfFile) throws InterruptedException, ExecutionException {
        formula.clear(); // Clears the list from the last one
        try {
            // Grab the file from the parameters
            Scanner scanner = new Scanner(cnfFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("c")) {
                    continue;
                }

                if (line.startsWith("p")) {
                    String[] args = line.split(" ");
                    int numVars = Integer.parseInt(args[2]); //variables
                    int numClauses = Integer.parseInt(args[3]); //clauses
                    maxNumVars = numVars;

                    for (int i = 0; i < numClauses; i++) {
                        //initialize a new clause to store the variables
                        Clause newClause = new Clause(numVars, maxNumVars, new LinkedList<Integer>());
                        if (scanner.hasNextLine()) {
                            String[] varStrings = scanner.nextLine().trim().split(" ");
                            for (String varString : varStrings) {
                                int var = Integer.parseInt(varString);
                                if (var != 0) {
                                    newClause.addVariable(var);
                                }
                            }
                            // Add clause to formula
                            formula.add(new Clause[]{newClause});
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // print the list for the user to see
        System.out.println("Working on: " + cnfFile.getPath());
        System.out.println("Maximum Variables of Variables: " + maxNumVars + ", Number of Clauses: " + formula.size());
        System.out.println("Amount of variations: " + createVariations(maxNumVars).size());
        System.out.println("Satisfiable: " + isSatisfiable(formula, createVariations(maxNumVars)));
        System.out.println("--------------");
    }

    public static ArrayList<int[]> createVariations(int inputVars) {
        int numVars = inputVars;
        ArrayList<int[]> variations = new ArrayList<>();

        //initialize first variation
        int[] variableValues = new int[numVars];

        // execute this 2^(inputVars) times
        for (int i = 0; i < Math.pow(2, numVars); i++) {
            //add current set of variations to the variation list
            variations.add(variableValues);
            // do i have to put the code to test for variation satisfiability here?????????????????????????????????????????????????????????????????????????
            // get the next variation and set it to vars
            variableValues = getNextVariation(variableValues);
        }

        return variations;
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

    public static boolean isSatisfiable(ArrayList<Clause[]> formula, ArrayList<int[]> vars) {
        return true;
    }
}




