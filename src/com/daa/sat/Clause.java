package com.daa.sat;

import java.util.LinkedList;


/**
 * SAT Bruteforcer
 * Justin Pitera
 * Finalized Date: 2.26.2023
 * Design and Analysis of Algorithms - Spring 2023
 * -----------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * bruteforce.Clause.java
 * The bruteforce.Clause class serves as the outlines to a bruteforce.Clause object, a Linked List that holds variables.
 */
public class Clause
{

    /**
     * The linked list of variables, where each variable is represented as an integer in a linked list.
     */
    private LinkedList<Integer> variables = new LinkedList<Integer>();


    /**
     * Default constructor for bruteforce.Clause object
     * @param variables The variables to be added to the clause
     */
    public Clause(LinkedList<Integer> variables)
    {
        this.variables = variables;
    }


    /**
     * Add a variable to the clause
     * @param variable The variables to be added to the clause object
     */
    public void addVariable(int variable)
    {
        variables.add(variable);
    }


    /**
     * Returns a string representation of the object
     */
    @Override
    public String toString() {
        return variables.toString();
    }


    /**
     * Returns the list of variables from the clause
     */
    public LinkedList<Integer> getVariables() {
        return variables;
    }

}