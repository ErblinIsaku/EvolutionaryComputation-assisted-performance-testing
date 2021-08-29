//  MOCell.java
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

package jmetal.metaheuristics.mocell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import jmetal.Qlearning_Ramdom_Dummy.*;
import jmetal.core.*;
import jmetal.problems.LoadTesting;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Neighborhood;
import jmetal.util.Ranking;
import jmetal.util.archive.CrowdingArchive;
import jmetal.util.comparators.CrowdingComparator;
import jmetal.util.comparators.DominanceComparator;

import java.util.*;

import jmetal.problems.WorkflowScheduling;
import org.apache.jmeter.engine.StandardJMeterEngine;


/**
 * This class represents an asynchronous version of MOCell algorithm, combining
 * aMOCell2 and aMOCell3. It is the aMOCell4 variant described in:
 *    A.J. Nebro, J.J. Durillo, F. Luna, B. Dorronsoro, E. Alba
 *   "Design Issues in a Multiobjective Cellular Genetic Algorithm."
 *   Evolutionary Multi-Criterion Optimization. 4th International Conference,
 *   EMO 2007. Sendai/Matsushima, Japan, March 2007.
 */
public class MOCell extends Algorithm{
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

    public MOCell(Problem problem){
        super (problem) ;
    }



    /** Execute the algorithm
     * @throws JMException */
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        //Init the parameters
        int populationSize, archiveSize, maxEvaluations, evaluations;
        Operator mutationOperator, crossoverOperator, selectionOperator;
        SolutionSet currentPopulation;
        CrowdingArchive archive;
        SolutionSet [] neighbors;
        Neighborhood neighborhood;
        Comparator dominance = new DominanceComparator();
        Comparator crowdingComparator = new CrowdingComparator();
        Distance distance = new Distance();





        // Read the parameters
        populationSize    = ((Integer)getInputParameter("populationSize")).intValue();
        archiveSize       = ((Integer)getInputParameter("archiveSize")).intValue();
        maxEvaluations    = ((Integer)getInputParameter("maxEvaluations")).intValue();

        // Read the operators
        mutationOperator  = operators_.get("mutation");
        crossoverOperator = operators_.get("crossover");
        selectionOperator = operators_.get("selection");

        // Initialize the variables
        currentPopulation  = new SolutionSet(populationSize);
        archive            = new CrowdingArchive(archiveSize,problem_.getNumberOfObjectives());
        evaluations        = 0;
        neighborhood       = new Neighborhood(populationSize);
        neighbors          = new SolutionSet[populationSize];


        // Fetching the tasks of the WorkFlow

//        List WorkflowList= new LinkedList();
//        try{
//            File file = new File("examples/Workflow.txt");
//            FileReader fileReader = new FileReader(file);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                WorkflowList.add(line);
//
//            }
//            fileReader.close();
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
//
//        List OptimalWorkflowList= new LinkedList();
//        for(int i=0; i<WorkflowList.size(); i++)
//        {
//            String level = (String)WorkflowList.get(i);
//            String[] levelTasks= level.split(",");
//
//            List levelTasksList= new LinkedList();
//            for(int j=0; j<levelTasks.length; j++)
//            {
//                levelTasksList.add(levelTasks[j]);
//
//            }
//            OptimalWorkflowList.add(levelTasksList);
//        }
//
//        List jobDependancyList= new LinkedList();
//
//        try{
//            File file = new File("examples/JobDependancy.txt");
//            FileReader fileReader = new FileReader(file);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                jobDependancyList.add(line);
//
//            }
//            fileReader.close();
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }

        // Create the initial population
//    OptorSimParameters _params;
//       OptorSimMain optorSimMainInstance = new OptorSimMain();
//       System.out.println("OptorSimMain> using default parameters file examples/parameters3.conf");
//       OptorSimParameters.setFilename("examples/parameters3.conf");
//       _params = OptorSimParameters.getInstance();
//			// Initialise networkInfo
//        GridConfFileReader gridconffilereader = GridConfFileReader.getInstance();
//
//        //initStorageElements();
//        JobConfFileReader jread = JobConfFileReader.getInstance();
//        Iterator iFiles = jread.assignFilesToSites();
//
//        ReplicaManager rm = ReplicaManager.getInstance();
//
//        while( iFiles.hasNext()) {
//                DataFile file = (DataFile) iFiles.next();
//                rm.registerEntry( file);
//        }

//      Solution indivitual;
//     int numOfPop=0;
//     while (numOfPop  < populationSize) {
//      indivitual = new Solution(problem_, OptimalWorkflowList);
//     ((WorkflowScheduling)problem_).evaluateConstraints(indivitual, OptimalWorkflowList);
//     if (indivitual.getNumberOfViolatedConstraint()==0)
//     {   numOfPop++;
//      ((WorkflowScheduling)problem_).evaluate(indivitual, OptimalWorkflowList, jobDependancyList);
//      evaluations++;
//      currentPopulation.add(indivitual);
//     }
//    }

        HashMap<String, Integer> WorkloadList = new HashMap<String, Integer>();

        {
//      WorkloadList.put(this.name, this.workload);
//      for (String i : WorkloadList.keySet()) {
//        Initialize();
            SUT_env = new SUT();
            //initializing first state
            SUT_env.applyAction();
            curr_SUT_state = SUT_env.getSUTState();
            StandardJMeterEngine jmeter = new StandardJMeterEngine();
            LoadTesting LoadTester = new LoadTesting();
            LoadTester.Initialize();
//        applyAction();
//        try {
//          executeTestPlan();
//        } catch (Exception e) {
//          e.printStackTrace();
//        }


//        System.out.println("Transaction: " + i + " Workload: " + WorkloadList.get(i));
//      }

//            Initialize();
//
//            System.out.println("Nameffgf: " + i + " Workload: " + WorkloadList.get(i));
//
//        }
        }

      for (int i = 0; i < populationSize; i++){
      Solution individual = new Solution(problem_, WorkloadList);
      ((LoadTesting)problem_).evaluate(individual, WorkloadList);
//      ((WorkflowScheduling)problem_).evaluateConstraints(individual, OptimalWorkflowList);
      currentPopulation.add(individual);
      individual.setLocation(i);
      evaluations++;
    }



    // Main loop
    while (evaluations < maxEvaluations){
      for (int ind = 0; ind < currentPopulation.size(); ind++){
        Solution individual = new Solution(currentPopulation.get(ind));

        Solution [] parents = new Solution[2];
        Solution [] offSpring;

        //neighbors[ind] = neighborhood.getFourNeighbors(currentPopulation,ind);
        neighbors[ind] = neighborhood.getEightNeighbors(currentPopulation,ind);
        neighbors[ind].add(individual);

        // parents
        parents[0] = (Solution)selectionOperator.execute(neighbors[ind]);
        if (archive.size() > 0) {
          parents[1] = (Solution)selectionOperator.execute(archive);
        } else {
          parents[1] = (Solution)selectionOperator.execute(neighbors[ind]);
        }

        // Create a new individual, using genetic operators mutation and crossover
        offSpring = (Solution [])crossoverOperator.execute(parents);
        mutationOperator.execute(offSpring[0]);

        // Evaluate individual an his constraints
        ((LoadTesting)problem_).evaluate(offSpring[0], WorkloadList);
//        ((WorkflowScheduling)problem_).evaluateConstraints(offSpring[0], OptimalWorkflowList);
        evaluations++;

        int flag = dominance.compare(individual,offSpring[0]);

        if (flag == 1) { //The new individual dominates
          offSpring[0].setLocation(individual.getLocation());
          currentPopulation.replace(offSpring[0].getLocation(),offSpring[0]);
          archive.add(new Solution(offSpring[0]));
        } else if (flag == 0) { //The new individual is non-dominated
          neighbors[ind].add(offSpring[0]);
          offSpring[0].setLocation(-1);
          Ranking rank = new Ranking(neighbors[ind]);
          for (int j = 0; j < rank.getNumberOfSubfronts(); j++) {
            distance.crowdingDistanceAssignment(rank.getSubfront(j),
                                                problem_.getNumberOfObjectives());
          }
          Solution worst = neighbors[ind].worst(crowdingComparator);

          if (worst.getLocation() == -1) { //The worst is the offspring
            archive.add(new Solution(offSpring[0]));
          } else {
            offSpring[0].setLocation(worst.getLocation());
            currentPopulation.replace(offSpring[0].getLocation(),offSpring[0]);
            archive.add(new Solution(offSpring[0]));
          }
        }
      }
    }
    archive.printFeasibleFUN("FUN_MOCell") ;
        Ranking ranking = new Ranking(currentPopulation);
//        ranking.getSubfront(0).printFeasibleFUN("FUN_SPEA2") ;

        return ranking.getSubfront(0);

    } // while
} // MOCell

