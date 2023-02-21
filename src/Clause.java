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

    // Get literals                   x:  1  2  3  4  5
    // [4, -3, 2] with 5 vars should be: [0, 1, 0, 1, 0]
    // In terms of the bruteforcer, if you find a variation in 2^5 where
    // currentVariation = [0, 1, 0, 1, 0] then the clause is satisfied.
    // Now do this for all of the clauses in the formula
    // if all of the clauses are satisfied, the formula is satisfied



}
