/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jmetal.problems;

import jmetal.Qlearning_Ramdom_Dummy.*;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.*;
import jmetal.encodings.variable.ArrayInt;
import jmetal.encodings.variable.Int;
import jmetal.util.JMException;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.SearchByClass;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Erblin
 */
public class LoadTesting extends Problem {

    public SUTstate curr_SUT_state;
    private SUT SUT_env;
    public LoadTester loadTester;
    public QualityMeasures qualityMeasures;
    private int initialWorkLoadPerTransaction;
    private double workLoadIncreasingStepRatio;
    private double threadPerSecond;
    public Transaction[] transactions;
    public String name;
    public int workload;
    public QLearning QL;
    private Random random;

    private int iterationNumber = 1;
    private int iterationStep = 1;
    private int episodeExecutionDelay;
    int maxResposeTimeThreshold = 1500;
    double maxErrorRateThreshold = 0.2;

    HashMap<String, Integer> WorkloadList = new HashMap<String, Integer>();

    {
        // Add keys and values (Name, Workload)

        Initialize();
        try {
            executeTestPlan();
        } catch (Exception e) {
            e.printStackTrace();
        }

        StandardJMeterEngine jmeter = new StandardJMeterEngine();
        LoadTester LoadTester = new LoadTester();
        LoadTester.Initialize();
        SUT_env = new SUT();
        // initializing first state
        SUT_env.applyAction();
        curr_SUT_state = SUT_env.getSUTState();

    }

    public LoadTesting() {
        Initialize();
    }

    public void Initialize() {
        this.threadPerSecond = 10.00;
        this.initialWorkLoadPerTransaction = 3;
        this.workLoadIncreasingStepRatio = 4.0 / 3.0;

        loadTester = new LoadTester();
        transactions = new Transaction[12];
        qualityMeasures = new QualityMeasures();

        transactions[0] = new Transaction("HomePage", initialWorkLoadPerTransaction);
        transactions[1] = new Transaction("RegisterPage", initialWorkLoadPerTransaction);
        transactions[2] = new Transaction("RegisterUser", initialWorkLoadPerTransaction);
        transactions[3] = new Transaction("BrowsePage", initialWorkLoadPerTransaction);
        transactions[4] = new Transaction("BrowseInCategory", initialWorkLoadPerTransaction);
        transactions[5] = new Transaction("BrowseInRegions", initialWorkLoadPerTransaction);
        transactions[6] = new Transaction("SellPage", initialWorkLoadPerTransaction);
        transactions[7] = new Transaction("SellItem", initialWorkLoadPerTransaction);
        transactions[8] = new Transaction("AboutMePage", initialWorkLoadPerTransaction);
        transactions[9] = new Transaction("AboutMeUser", initialWorkLoadPerTransaction);
        transactions[10] = new Transaction("BidOnItem", initialWorkLoadPerTransaction);
        transactions[11] = new Transaction("SellItem", initialWorkLoadPerTransaction);

    }

    public static class LoadTester {
        private StandardJMeterEngine jmeter;
        private Summariser summer;
        private ResultCollector logger;

        private String JMETER_HOME_PATH = "/Users/rise/Desktop/apache-jmeter-3.1/apache-jmeter-3.1";
        private String JMETER_PROPERTY_PATH = "/Users/rise/Desktop/apache-jmeter-3.1/apache-jmeter-3.1/bin/jmeter.properties";
        private String JMETER_LOG_FILE_PATH = "/Users/rise/Desktop/transactions/all_transactions.jtl";
        private String JMX_FILES_PATH = "/Users/rise/Desktop/transactions/";

        public LoadTester() {
            Initialize();
        }

        public void Initialize() {

            // JMeter Engine
            jmeter = new StandardJMeterEngine();
            // Initialize Properties, logging, locale, etc.
            JMeterUtils.loadJMeterProperties(JMETER_PROPERTY_PATH);
            JMeterUtils.setJMeterHome(JMETER_HOME_PATH);
            // JMeterUtils.initLogging();// you can comment this line out to see extra log
            // messages of i.e. DEBUG level
            JMeterUtils.initLocale();
            // Initialize JMeter SaveService
            try {
                SaveService.loadProperties();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public boolean ExecuteAllTransactions(Transaction[] transactions, int rampUpTime, int numOfLoops,
                QualityMeasures qualityMeasures) {

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
            try {
                testPlanTree = SaveService.loadTree(new File(JMX_FILES_PATH + "all_transactions_local_server.jmx"));

                // testPlanTree.add(testPlanTree.getArray()[0], logger);
                // System.out.println(testPlanTree.toString());

                SearchByClass<ThreadGroup> threadGroups = new SearchByClass<>(ThreadGroup.class);
                testPlanTree.traverse(threadGroups);
                Collection<ThreadGroup> threadGroupsRes = threadGroups.getSearchResults();
                for (ThreadGroup threadGroup : threadGroupsRes) {
                    for (Transaction t : transactions) {
                        if ((t.name + "_thread_group").equals(threadGroup.getName())) {

                            threadGroup.setNumThreads(t.workLoad);
                            threadGroup.setRampUp(rampUpTime);
                            ((LoopController) threadGroup.getSamplerController()).setLoops(numOfLoops);

                            // System.out.println("thread group: " + threadGroup.getName());
                            // System.out.println("Transaction: " + t.name + ", Workload (num of threads): "
                            // + threadGroup.getProperty("ThreadGroup.num_threads").toString());
                            // System.out.println("Ramp Up:" + threadGroup.getRampUp());
                            // System.out.println("Loop: " + ((LoopController)
                            // threadGroup.getSamplerController()).getProperty("LoopController.loops"));
                            break;
                        }
                    }
                }
                System.out.println("Ramp Up:" + rampUpTime);

                MyResultCollector myResultCollector = new MyResultCollector(summer);
                testPlanTree.add(testPlanTree.getArray()[0], myResultCollector);

                // Run JMeter Test
                jmeter.configure(testPlanTree);
                try {
                    jmeter.run();
                    testPassed = myResultCollector.allTestSamplesPassed();
                    if (testPassed)
                        myResultCollector.calculateAverageQualityMeasures(qualityMeasures);

                } catch (Exception e) {
                    System.out.println("jmeter run");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.out.println("testPlanTree");
                e.printStackTrace();
            }

            return testPassed;
        }

    }

    public void applyAction() // Modifying load of all Transactions then executing the test
    {
        boolean success = false;

        for (Transaction t : transactions) {
            int prevWorkLoad = t.workload;
            t.workload = (int) (prevWorkLoad * workLoadIncreasingStepRatio);

        }

        // body of action
        while (!success) {
            try {
                success = executeTestPlan();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void applyAction_base() // executing the test
    {
        boolean success = false;
        // body of action
        while (!success) {
            try {
                success = executeTestPlan();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void applyAction(int action) // Modifying load of a chosen Transaction then executing the test
    {
        boolean success = false;

        int prevWorkLoad = transactions[action].workLoad;
        transactions[action].workLoad = (int) (prevWorkLoad * workLoadIncreasingStepRatio);

        // body of action
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
        int rampUpTime = (int) Math.round((double) GetTotalWorkLoad() / threadPerSecond);
        success = loadTester.ExecuteAllTransactions(transactions, rampUpTime, 1, qualityMeasures);

        return success;
    }

    public int GetTotalWorkLoad() {
        int totalWorkLoad = 0;
        for (Transaction t : transactions)
            totalWorkLoad += t.workLoad;
        return totalWorkLoad;
    }

    public Double CalculateReward() {
        double normalizedResponseTime = curr_SUT_state.qualityMeasures.responseTime * 100 / maxResposeTimeThreshold;
        double normalizedErrorRate = curr_SUT_state.qualityMeasures.errorRate * 100 / maxErrorRateThreshold;
        double reward = Math.pow(normalizedResponseTime, 2) + Math.pow(normalizedErrorRate, 2);

        return reward;
    }

    public boolean isDone() {
        boolean done = false;

        // if (prev_SUT_state.qualityMeasures.errorRate >
        // curr_SUT_state.qualityMeasures.errorRate ||
        // prev_SUT_state.qualityMeasures.responseTime >
        // curr_SUT_state.qualityMeasures.responseTime)

        if (curr_SUT_state.qualityMeasures.errorRate > maxErrorRateThreshold
                || curr_SUT_state.qualityMeasures.responseTime > maxResposeTimeThreshold) {
            done = true;
            // this.logger.info("Mission ended");
            System.out.println("Mission ended");

        }

        return done;
    }

    public SUTstate getSUTState() {
        return new SUTstate(qualityMeasures, GetTotalWorkLoad(), transactions);
    }

    public int getRandomAction() {
        return random.nextInt(12);
    }

    public void logStatus(String transactionName) {
        System.out.println("iteration: " + iterationNumber + ", iterationStep: " + iterationStep);
        System.out.println(curr_SUT_state.toString());
        // this.logger.info(curr_SUT_state.toString());
    }

    public LoadTesting(String solutionType, Integer numberOfVariables) {
        numberOfVariables_ = numberOfVariables;
        numberOfObjectives_ = 2;
        numberOfConstraints_ = 0;
        problemName_ = "LoadTesting";

        upperLimit_ = new double[numberOfVariables_];
        lowerLimit_ = new double[numberOfVariables_];

        for (int var = 0; var < numberOfVariables_; var++) {
            lowerLimit_[var] = 3.0;
            upperLimit_[var] = 99.0;
        } // for

        // for (int i = 0; i < numberOfVariables_; i++) {
        // lowerLimit_[i] = 1 ;
        // upperLimit_[i] = numberOfVariables_ ;
        // } // for

        if (solutionType.compareTo("IntSolutionType") == 0)
            solutionType_ = new IntSolutionType(this);
        else if (solutionType.compareTo("Real") == 0)
            solutionType_ = new RealSolutionType(this);
        else if (solutionType.compareTo("ArrayReal") == 0)
            solutionType_ = new ArrayRealSolutionType(this);
        else if (solutionType.compareTo("ArrayIntSolutionType") == 0)
            solutionType_ = new ArrayIntSolutionType(this);

        else {
            System.out.println("Error: solution type " + solutionType + " invalid");
            System.exit(-1);
        }
    }

    public void evaluate(Solution solution) throws JMException {

    }

    public void evaluate(Solution solution, HashMap<String, Integer> WorkloadList) throws JMException {

        // Objectives
        double[] f = new double[2];
        Variable[] variable = solution.getDecisionVariables();
        double x1 = variable[0].getValue();

        this.maxResposeTimeThreshold = maxResposeTimeThreshold;
        this.maxErrorRateThreshold = maxErrorRateThreshold;
        this.episodeExecutionDelay = episodeExecutionDelay;
        iterationNumber = 1;
        iterationStep = 1;
        random = new Random();
        int currStep = 1;
        int action;

        // executing several episodes where in the end of each episode the SUT reaches
        // the error rate or response time threshold

        // while(iterationNumber <= maxIterationNumber){
        SUT_env = new SUT();
        curr_SUT_state = SUT_env.getSUTState();
        // executing each step in each episode
        while (!isDone()) {

            action = getRandomAction();
            SUT_env.applyAction(action);
            curr_SUT_state = SUT_env.getSUTState();
            double reward = CalculateReward();
            logStatus(SUT_env.transactions[action].name);

            x1 = SUT_env.transactions[action].workLoad;

            // x[0] = solution.getDecisionVariables()[0].getValue();
            // y[0] = solution.getDecisionVariables()[0].getValue();

            iterationStep = iterationStep + 1;
            int totalWorkload = SUT_env.getSUTState().workload;
            System.out.println("Reward:" + reward);
            double obj1;
            int obj2;

            // First Objective: Finding the reward for the transactions (Where response time
            // and error rate are exceded)
            obj1 = reward;
            f[0] = obj1;

            // Second Objective: Finding the optimal Workload where Response time or Error
            // rate is exceded (less number of virtual users).
            obj2 = totalWorkload;
            // System.out.println( "Workloaddddd:" +obj2);
            f[1] = obj2;

        }
        currStep = currStep + iterationStep;
        iterationStep = 1;
        iterationNumber = iterationNumber + 1;
        // delaying the execution of next state to let the SUT go to a normal situation

        try {
            TimeUnit.MINUTES.sleep(episodeExecutionDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        solution.setObjective(0, f[0]);
        solution.setObjective(1, f[1]);

    }
}
