//  SPEA2.java
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

package jmetal.metaheuristics.spea2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jmetal.Qlearning_Ramdom_Dummy.*;
import jmetal.core.*;
import jmetal.encodings.variable.ArrayInt;
import jmetal.problems.LoadTesting;
import jmetal.problems.WorkflowScheduling;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.Spea2Fitness;
import org.apache.jmeter.engine.StandardJMeterEngine;

/**
 * This class representing the SPEA2 algorithm
 */
public class SPEA2 extends Algorithm {
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
  public QLearning QL;

  /**
   * Defines the number of tournaments for creating the mating pool
   */
  public static final int TOURNAMENTS_ROUNDS = 1;

  /**
   * Constructor. Create a new SPEA2 instance
   * 
   * @param problem Problem to solve
   */
  public SPEA2(Problem problem) {
    super(problem);
  } // Spea2

  /**
   * Runs of the Spea2 algorithm.
   * 
   * @return a <code>SolutionSet</code> that is a set of non dominated solutions
   *         as a result of the algorithm execution
   * @throws JMException
   */
  public SolutionSet execute() throws JMException, ClassNotFoundException {
    int populationSize, archiveSize, maxEvaluations, evaluations;
    Operator crossoverOperator, mutationOperator, selectionOperator;
    SolutionSet solutionSet, archive, offSpringSolutionSet;

    // Read the params
    populationSize = ((Integer) getInputParameter("populationSize")).intValue();
    archiveSize = ((Integer) getInputParameter("archiveSize")).intValue();
    maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();

    // Read the operators
    crossoverOperator = operators_.get("crossover");
    mutationOperator = operators_.get("mutation");
    selectionOperator = operators_.get("selection");

    // Initialize the variables
    solutionSet = new SolutionSet(populationSize);
    archive = new SolutionSet(archiveSize);
    evaluations = 0;

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

    Solution newSolution;
    int numOfPop = 0;
    while (numOfPop < populationSize) {
      newSolution = new Solution(problem_, WorkloadList);

      if (newSolution.getNumberOfViolatedConstraint() == 0) {
        numOfPop++;
        ((LoadTesting) problem_).evaluate(newSolution, WorkloadList);
        evaluations++;
        solutionSet.add(newSolution);
      }
    }

    while (evaluations < maxEvaluations) {
      SolutionSet union = solutionSet.union(archive);
      // Spea2Fitness spea = new Spea2Fitness(union);
      // spea.fitnessAssign();
      // archive = spea.environmentalSelection(archiveSize);
      // Create a new offspringPopulation
      offSpringSolutionSet = new SolutionSet(populationSize);
      Solution[] parents = new Solution[2];
      while (offSpringSolutionSet.size() < populationSize) {
        int j = 0;
        do {
          j++;
          parents[0] = (Solution) selectionOperator.execute(archive);
        } while (j < SPEA2.TOURNAMENTS_ROUNDS); // do-while
        int k = 0;
        do {
          k++;
          parents[1] = (Solution) selectionOperator.execute(archive);
        } while (k < SPEA2.TOURNAMENTS_ROUNDS); // do-while

        // make the crossover
        Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
        mutationOperator.execute(offSpring[0]);
        ((LoadTesting) problem_).evaluate(offSpring[0], WorkloadList);

        offSpringSolutionSet.add(offSpring[0]);
        evaluations++;
      } // while
      // End Create a offSpring solutionSet
      solutionSet = offSpringSolutionSet;
    } // while

    // Ranking ranking = new Ranking(archive);
    // ranking.getSubfront(0).printFeasibleFUN("FUN_SPEA2") ;

    // return ranking.getSubfront(0);

    // Ranking ranking = new Ranking(archive);
    // return ranking.getSubfront(0);

    Ranking ranking = new Ranking(solutionSet);
    ranking.getSubfront(0).printFeasibleFUN("FUN_SPEA2");

    return ranking.getSubfront(0);

  } // execute
} // SPEA2
