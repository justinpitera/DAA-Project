import com.sun.jdi.IntegerValue;

import java.util.LinkedList;
import java.util.List;

public class Clause
{

    private int numVars; //stores how many variables are in actually in the clause
    private int numClauses;
    private LinkedList<Integer> variables = new LinkedList<Integer>();

    public Clause(int numVars, int numClauses, LinkedList<Integer> variables)
    {
        this.numVars = numVars;
        this.numClauses = numClauses;
        this.variables = variables;
    }

    public void addVariable(int var)
    {
        variables.add(var);
    }

    @Override
    public String toString() {
        return variables.toString();
    }

    public List<Integer> getVariables() {
        return variables;
    }

    public int get(int i)
    {
        return variables.get(i);
    }



    //Unused but i kept it in there because i spent a while on it and i cant stop looking at it like it has to be important to some degree right? right.....?
    public int[] getLiterals() {
        // Initialize literal with all false values up to numVars number of variables
        int[] literals = new int[numVars];
        // Go through each numVar
        for (int i = 0; i < numVars; i++)
        {
            literals[i] = 0;
            //Set current var to literal value in literal
            for (int var : variables)
            {
                int literalValue;
                if (var > 0)
                {
                    literalValue = 1;
                } else
                {
                    literalValue = 0;
                }
                literals[Math.abs(var) - 1] = literalValue;
            }
        }
        // Return literals
        return literals;
    }


}
