//  PAES.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jmetal.metaheuristics.paes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import jmetal.Qlearning_Ramdom_Dummy.QualityMeasures;
import jmetal.Qlearning_Ramdom_Dummy.SUT;
import jmetal.Qlearning_Ramdom_Dummy.SUTstate;
import jmetal.Qlearning_Ramdom_Dummy.Transaction;
import jmetal.core.*;
import jmetal.problems.LoadTesting;
import jmetal.util.JMException;
import jmetal.util.archive.AdaptiveGridArchive;
import jmetal.util.comparators.DominanceComparator;

import java.util.*;
import org.apache.jmeter.engine.StandardJMeterEngine;

/**
 * This class implements the PAES algorithm.
 */
public class PAES extends Algorithm {
    public SUTstate curr_SUT_state;
    private SUT SUT_env;
    public LoadTesting.LoadTester loadTester;
    public QualityMeasures qualityMeasures;
    private int initialWorkLoadPerTransaction;
    private double workLoadIncreasingStepRatio;
    private double threadPerSecond;
    public Transaction[] transactions;
    public String name;
    public int workload;

    /**
     * Create a new PAES instance for resolve a problem
     * 
     * @param problem Problem to solve
     */
    public PAES(Problem problem) {
        super(problem);
    } // Paes

    /**
     * Tests two solutions to determine which one becomes be the guide of PAES
     * algorithm
     * 
     * @param solution        The actual guide of PAES
     * @param mutatedSolution A candidate guide
     */
    public Solution test(Solution solution, Solution mutatedSolution, AdaptiveGridArchive archive) {

        int originalLocation = archive.getGrid().location(solution);
        int mutatedLocation = archive.getGrid().location(mutatedSolution);

        if (originalLocation == -1) {
            return new Solution(mutatedSolution);
        }

        if (mutatedLocation == -1) {
            return new Solution(solution);
        }

        if (archive.getGrid().getLocationDensity(mutatedLocation) < archive.getGrid()
                .getLocationDensity(originalLocation)) {
            return new Solution(mutatedSolution);
        }

        return new Solution(solution);
    } // test

    /**
     * Runs of the Paes algorithm.
     * 
     * @return a <code>SolutionSet</code> that is a set of non dominated solutions
     *         as a result of the algorithm execution
     * @throws JMException
     */
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        int bisections, archiveSize, maxEvaluations, evaluations;
        AdaptiveGridArchive archive;
        Operator mutationOperator;
        Comparator dominance;

        // Read the params
        bisections = ((Integer) this.getInputParameter("biSections")).intValue();
        archiveSize = ((Integer) this.getInputParameter("archiveSize")).intValue();
        maxEvaluations = ((Integer) this.getInputParameter("maxEvaluations")).intValue();

        // Read the operators
        mutationOperator = this.operators_.get("mutation");

        // Initialize the variables
        evaluations = 0;
        archive = new AdaptiveGridArchive(archiveSize, bisections, problem_.getNumberOfObjectives());
        dominance = new DominanceComparator();

        HashMap<String, Integer> WorkloadList = new HashMap<String, Integer>();

        {
            // WorkloadList.put(this.name, this.workload);
            // for (String i : WorkloadList.keySet()) {
            // Initialize();
            SUT_env = new SUT();
            // initializing first state
            SUT_env.applyAction();
            curr_SUT_state = SUT_env.getSUTState();
            StandardJMeterEngine jmeter = new StandardJMeterEngine();
            LoadTesting LoadTester = new LoadTesting();
            LoadTester.Initialize();
        }

        Solution solution = new Solution(problem_);
        ((LoadTesting) problem_).evaluate(solution, WorkloadList);
        // ((LoadTesting)problem_).evaluateConstraints(solution);
        evaluations++;

        // Add it to the archive
        archive.add(new Solution(solution));

        // Iterations....
        do {
            // Create the mutate one
            Solution mutatedIndividual = new Solution(solution);
            mutationOperator.execute(mutatedIndividual);

            ((LoadTesting) problem_).evaluate(mutatedIndividual, WorkloadList);
            // ((LoadTesting)problem_).evaluateConstraints(mutatedIndividual, WorkloadList);
            evaluations++;
            // <-

            // Check dominance
            int flag = dominance.compare(solution, mutatedIndividual);

            if (flag == 1) { // If mutate solution dominate
                solution = new Solution(mutatedIndividual);
                archive.add(mutatedIndividual);
            } else if (flag == 0) { // If none dominate the other
                if (archive.add(mutatedIndividual)) {
                    solution = test(solution, mutatedIndividual, archive);
                }
            }
            /*
             * if ((evaluations % 100) == 0) {
             * archive.printObjectivesToFile("FUN"+evaluations) ;
             * archive.printVariablesToFile("VAR"+evaluations) ;
             * archive.printObjectivesOfValidSolutionsToFile("FUNV"+evaluations) ; }
             */
        } while (evaluations < maxEvaluations);

        // Return the population of non-dominated solution
        archive.printFeasibleFUN("FUN_PAES");

        return archive;
    } // execute
} // PAES
