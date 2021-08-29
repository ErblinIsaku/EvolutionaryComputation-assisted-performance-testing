//  NSGAII.java
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

package jmetal.metaheuristics.nsgaII;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import jmetal.Qlearning_Ramdom_Dummy.*;
import jmetal.core.*;
import jmetal.problems.LoadTesting;
import jmetal.problems.WorkflowScheduling;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.comparators.CrowdingComparator;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.SearchByClass;


/** 
 *  Implementation of NSGA-II.
 *  This implementation of NSGA-II makes use of a QualityIndicator object
 *  to obtained the convergence speed of the algorithm. This version is used
 *  in the paper:
 *     A.J. Nebro, J.J. Durillo, C.A. Coello Coello, F. Luna, E. Alba 
 *     "A Study of Convergence Speed in Multi-Objective Metaheuristics." 
 *     To be presented in: PPSN'08. Dortmund. September 2008.
 */

public class NSGAII extends Algorithm {
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

//  int maxResposeTimeThreshold = 1500;
//  double maxErrorRateThreshold = 0.2;
//



  /**
   * Constructor
   * @param problem Problem to solve
   */
  public NSGAII(Problem problem) {
    super (problem) ;
  } // NSGAII

  /**   
   * Runs the NSGA-II algorithm.
   * @return a <code>SolutionSet</code> that is a set of non dominated solutions
   * as a result of the algorithm execution
   * @throws JMException 
   */
  public SolutionSet execute() throws JMException, ClassNotFoundException {
    int populationSize;
    int maxEvaluations;
    int evaluations;

    QualityIndicator indicators; // QualityIndicator object
    int requiredEvaluations; // Use in the example of use of the
    // indicators object (see below)

    SolutionSet population;
    SolutionSet offspringPopulation;
    SolutionSet union;

    Operator mutationOperator;
    Operator crossoverOperator;
    Operator selectionOperator;

    Distance distance = new Distance();

    //Read the parameters
    populationSize = ((Integer) getInputParameter("populationSize")).intValue();
    maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();
    indicators = (QualityIndicator) getInputParameter("indicators");

    //Initialize the variables
    population = new SolutionSet(populationSize);
    evaluations = 0;

    requiredEvaluations = 0;

    //Read the operators
    mutationOperator = operators_.get("mutation");
    crossoverOperator = operators_.get("crossover");
    selectionOperator = operators_.get("selection");


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
    // Fetching the tasks of the WorkFlow
       
//        List WorkflowList= new LinkedList();
//        try{
//        File file = new File("examples/Workflow.txt");
//	FileReader fileReader = new FileReader(file);
//	BufferedReader bufferedReader = new BufferedReader(fileReader);
//
//	String line;
//        while ((line = bufferedReader.readLine()) != null) {
//        WorkflowList.add(line);
//
//			}
//	fileReader.close();
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
//
//        List OptimalWorkflowList= new LinkedList();
//        for(int i=0; i<WorkflowList.size(); i++)
//       {
//           String level = (String)WorkflowList.get(i);
//           String[] levelTasks= level.split(",");
//
//           List levelTasksList= new LinkedList();
//            for(int j=0; j<levelTasks.length; j++)
//           {
//             levelTasksList.add(levelTasks[j]);
//
//           }
//            OptimalWorkflowList.add(levelTasksList);
//       }
//
//        List jobDependancyList= new LinkedList();
//
//        try{
//        File file = new File("examples/JobDependancy.txt");
//	FileReader fileReader = new FileReader(file);
//	BufferedReader bufferedReader = new BufferedReader(fileReader);
//
//	String line;
//        while ((line = bufferedReader.readLine()) != null) {
//        jobDependancyList.add(line);
//
//			}
//	fileReader.close();
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
    
    
    // Create the initial solutionSet
//       OptorSimParameters _params;
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
        
    Solution newSolution;
     int numOfPop=0;
   while (numOfPop  < populationSize) {
//      newSolution = new Solution(problem_, Arrays.asList(this.transactions));
     newSolution = new Solution(problem_, WorkloadList);

     if (newSolution.getNumberOfViolatedConstraint()==0)
     {   numOfPop++;
      ((LoadTesting)problem_).evaluate(newSolution,  WorkloadList);
      evaluations++;
      population.add(newSolution);
     }
    }        
    
    
//    for (int i = 0; i < populationSize; i++) {
//      newSolution = new Solution(problem_);
//      problem_.evaluate(newSolution);
//      problem_.evaluateConstraints(newSolution);
//      evaluations++;
//      population.add(newSolution);
//    } //for       

    // Generations 
    while (evaluations < maxEvaluations) {

      // Create the offSpring solutionSet      
      offspringPopulation = new SolutionSet(populationSize);
      Solution[] parents = new Solution[2];
      for (int i = 0; i < (populationSize / 2); i++) {
        if (evaluations < maxEvaluations) {
          //obtain parents
          parents[0] = (Solution) selectionOperator.execute(population);
          parents[1] = (Solution) selectionOperator.execute(population);
//          Solution[] offSpring = (Solution[]) selectionOperator.execute(parents);

          Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
          mutationOperator.execute(offSpring[0]);
          mutationOperator.execute(offSpring[1]);
          ((LoadTesting)problem_).evaluate(offSpring[0], WorkloadList);

          ((LoadTesting)problem_).evaluate(offSpring[1], WorkloadList);

          offspringPopulation.add(offSpring[0]);
          offspringPopulation.add(offSpring[1]);
          evaluations += 2;
        } // if                            
      } // for

      // Create the solutionSet union of solutionSet and offSpring
      union = population.union(offspringPopulation);

      // Ranking the union
      Ranking ranking = new Ranking(union);

      int remain = populationSize;
      int index = 0;
      SolutionSet front = null;
      population.clear();

      // Obtain the next front
      front = ranking.getSubfront(index);

      while ((remain > 0) && (remain >= front.size())) {
        //Assign crowding distance to individuals
        distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
        //Add the individuals of this front
        for (int k = 0; k < front.size(); k++) {
          population.add(front.get(k));
        } // for

        //Decrement remain
        remain = remain - front.size();

        //Obtain the next front
        index++;
        if (remain > 0) {
          front = ranking.getSubfront(index);
        } // if        
      } // while

      // Remain is less than front(index).size, insert only the best one
      if (remain > 0) {  // front contains individuals to insert                        
        distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
        front.sort(new CrowdingComparator());
        for (int k = 0; k < remain; k++) {
          population.add(front.get(k));
        } // for

        remain = 0;
      } // if                               

      // This piece of code shows how to use the indicator object into the code
      // of NSGA-II. In particular, it finds the number of evaluations required
      // by the algorithm to obtain a Pareto front with a hypervolume higher
      // than the hypervolume of the true Pareto front.
      if ((indicators != null) &&
          (requiredEvaluations == 0)) {
        double HV = indicators.getHypervolume(population);
        if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
          requiredEvaluations = evaluations;
        } // if
      } // if
    } // while

    // Return as output parameter the required evaluations
    setOutputParameter("evaluations", requiredEvaluations);

    // Return the first non-dominated front
    Ranking ranking = new Ranking(population);
    ranking.getSubfront(0).printFeasibleFUN("FUN_NSGAII") ;

    return ranking.getSubfront(0);
  } // execute



//  private void Initialize() {
//    this.threadPerSecond = 10.00;
//    this.initialWorkLoadPerTransaction = 3;
//    this.workLoadIncreasingStepRatio = 4.0 / 3.0;
//
//    loadTester = new LoadTesting.LoadTester();
//    transactions = new Transaction[12];
//    qualityMeasures = new QualityMeasures();
//
//    transactions[0] = new Transaction("HomePage", initialWorkLoadPerTransaction);
//    transactions[1] = new Transaction("RegisterPage", initialWorkLoadPerTransaction);
//    transactions[2] = new Transaction("RegisterUser", initialWorkLoadPerTransaction);
//    transactions[3] = new Transaction("BrowsePage", initialWorkLoadPerTransaction);
//    transactions[4] = new Transaction("BrowseInCategory", initialWorkLoadPerTransaction);
//    transactions[5] = new Transaction("BrowseInRegions", initialWorkLoadPerTransaction);
//    transactions[6] = new Transaction("SellPage", initialWorkLoadPerTransaction);
//    transactions[7] = new Transaction("SellItem", initialWorkLoadPerTransaction);
//    transactions[8] = new Transaction("AboutMePage", initialWorkLoadPerTransaction);
//    transactions[9] = new Transaction("AboutMeUser", initialWorkLoadPerTransaction);
//    transactions[10] = new Transaction("BidOnItem", initialWorkLoadPerTransaction);
//    transactions[11] = new Transaction("SellItem", initialWorkLoadPerTransaction);
//  }

  public static class LoadTester {
    private StandardJMeterEngine jmeter;
    private Summariser summer;
    private ResultCollector logger;

    private String JMETER_HOME_PATH = "/Users/rise/Desktop/apache-jmeter-3.1/apache-jmeter-3.1";
    private String JMETER_PROPERTY_PATH = "/Users/rise/Desktop/apache-jmeter-3.1/apache-jmeter-3.1/bin/jmeter.properties";
    private String JMETER_LOG_FILE_PATH = "/Users/rise/Desktop/transactions/PetStore/all_transactions_local_server.jtl";
    private String JMX_FILES_PATH = "/Users/rise/Desktop/transactions/PetStore/";

    public void Initialize() {

      // JMeter Engine
      jmeter = new StandardJMeterEngine();
      // Initialize Properties, logging, locale, etc.
      JMeterUtils.loadJMeterProperties(JMETER_PROPERTY_PATH);
      JMeterUtils.setJMeterHome(JMETER_HOME_PATH);
      // JMeterUtils.initLogging();// you can comment this line out to see extra log messages of i.e. DEBUG level
      JMeterUtils.initLocale();
      // Initialize JMeter SaveService
      try {
        SaveService.loadProperties();
      } catch (Exception e) {
        e.printStackTrace();
      }


    }
    public boolean ExecuteAllTransactions(Transaction[] transactions, int rampUpTime, int numOfLoops, QualityMeasures qualityMeasures){

      summer = null;
      String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
      if (summariserName.length() > 0) {
        summer = new Summariser(summariserName);
      }

      String logFile = JMETER_LOG_FILE_PATH;
      logger = new ResultCollector(summer);
      logger.setFilename(logFile);

      HashTree testPlanTree;
      boolean testPassed = false;
      String transactionName = "all_transactions";
      try{
        testPlanTree = SaveService.loadTree(new File (JMX_FILES_PATH+"all_transactions_local_server.jmx"));

        //  testPlanTree.add(testPlanTree.getArray()[0], logger);
        // System.out.println(testPlanTree.toString());

        SearchByClass<ThreadGroup> threadGroups = new SearchByClass<>(ThreadGroup.class);
        testPlanTree.traverse(threadGroups);
        Collection<ThreadGroup> threadGroupsRes = threadGroups.getSearchResults();
        for (ThreadGroup threadGroup : threadGroupsRes) {
          for (Transaction t : transactions) {
            if((t.name+"_thread_group").equals(threadGroup.getName())) {

              threadGroup.setNumThreads(t.workLoad);
              threadGroup.setRampUp(rampUpTime);
              ((LoopController) threadGroup.getSamplerController()).setLoops(numOfLoops);

              //System.out.println("thread group: " + threadGroup.getName());
              //System.out.println("Transaction: " + t.name + ", Workload (num of threads): " + threadGroup.getProperty("ThreadGroup.num_threads").toString());
              //System.out.println("Ramp Up:" + threadGroup.getRampUp());
              //System.out.println("Loop: " + ((LoopController) threadGroup.getSamplerController()).getProperty("LoopController.loops"));
              break;
            }
          }
        }
        System.out.println("Ramp Up:" + rampUpTime);


        MyResultCollector myResultCollector = new MyResultCollector(summer);
        testPlanTree.add(testPlanTree.getArray()[0], myResultCollector);

        // Run JMeter Test
        jmeter.configure(testPlanTree);
        try{
          jmeter.run();
          testPassed = myResultCollector.allTestSamplesPassed();
          if(testPassed)
            myResultCollector.calculateAverageQualityMeasures(qualityMeasures);


        }catch(Exception e){
          System.out.println("jmeter run");
          e.printStackTrace();
        }
      }catch(Exception e){
        System.out.println("testPlanTree");
        e.printStackTrace();
      }

      return testPassed;
    }




  }

    public void applyAction() //Modifying load of all Transactions then executing the test
    {
      boolean success = false;

      for(Transaction t : transactions)
      {
        int prevWorkLoad = t.workload;
        t.workload = (int) (prevWorkLoad * workLoadIncreasingStepRatio);
      }

      //body of action
      while (!success) {
        try {
          success = executeTestPlan();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    public void applyAction(int action) //Modifying load of a chosen Transaction then executing the test
    {
      boolean success = false;


      int prevWorkLoad = transactions[action].workLoad;
      transactions[action].workLoad = (int) (prevWorkLoad * workLoadIncreasingStepRatio);


      //body of action
      while (!success) {
        try {
          success = executeTestPlan();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  public boolean executeTestPlan() throws Exception {

    boolean success = false;
    int rampUpTime = (int) Math.round((double)  GetTotalWorkLoad()  / threadPerSecond);
    success = loadTester.ExecuteAllTransactions(transactions, rampUpTime,1,qualityMeasures);
    return success;
  }
  public int GetTotalWorkLoad() {
    int totalWorkLoad = 0;
    for (Transaction t : transactions) totalWorkLoad += t.workLoad;
    return totalWorkLoad;
  }

} // NSGA-II
