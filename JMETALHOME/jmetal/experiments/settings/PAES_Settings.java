//  PAES_Settings.java
//
//  Authors:
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

package jmetal.experiments.settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import jmetal.core.Algorithm;
import jmetal.experiments.Settings;
import jmetal.metaheuristics.paes.PAES;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.problems.LoadTesting;
import jmetal.problems.ProblemFactory;
import jmetal.util.JMException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import jmetal.problems.WorkflowScheduling;
import jmetal.qualityIndicator.QualityIndicator;

/**
 * Settings class of algorithm PAES
 */
public class PAES_Settings extends Settings{

  public int maxEvaluations_ ;
  public int archiveSize_    ;
  public int biSections_     ;
  public double mutationProbability_ ;
  public double mutationDistributionIndex_;

  /**
   * Constructor
   */
  public PAES_Settings(String problem) {
    super(problem) ;

//    Object [] problemParams = {"Real"};
//    try {
//      problem_ = (new ProblemFactory()).getProblem(problemName_, problemParams);
//    } catch (JMException e) {
//      e.printStackTrace();
//    }

//     List WorkflowList= new LinkedList();
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
//        int numOfTasks=0;
//
//
//        for(int i=0; i<WorkflowList.size(); i++)
//       {
//           String level = (String)WorkflowList.get(i);
//           String[] levelTasks= level.split(",");
//           numOfTasks = numOfTasks+levelTasks.length;
//
//       }

      problem_ = new LoadTesting("IntSolutionType", 2);


    // Default experiments.settings
    maxEvaluations_ = 20 ;
    archiveSize_    = 50   ;
    biSections_     = 5     ;
    mutationProbability_ = 0.4 ;
    mutationDistributionIndex_ = 20.0 ;
  } // PAES_Settings

  /**
   * Configure the MOCell algorithm with default parameter experiments.settings
   * @return an algorithm object
   * @throws jmetal.util.JMException
   */
  public Algorithm configure() throws JMException {
    Algorithm algorithm ;
    Mutation  mutation   ;

    HashMap  parameters ; // Operator parameters
    QualityIndicator indicators;

    // Creating the problem
    algorithm = new PAES(problem_) ;

    // Algorithm parameters
    algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
    algorithm.setInputParameter("biSections", biSections_);
    algorithm.setInputParameter("archiveSize",archiveSize_ );

    // Mutation (Real variables)
    parameters = new HashMap() ;
    parameters.put("probability", mutationProbability_) ;
    parameters.put("distributionIndex", mutationDistributionIndex_) ;
    mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);

    // Add the operators to the algorithm
    algorithm.addOperator("mutation", mutation);

    if((paretoFrontFile_!=null) &&(!paretoFrontFile_.equals(""))){
     indicators= new QualityIndicator(problem_,paretoFrontFile_);
     algorithm.setInputParameter("indicators",indicators);
    }

    return algorithm ;
  } // configure

  /**
   * Configure PAES with user-defined parameter experiments.settings
   * @return A PAES algorithm object
   */
  @Override
  public Algorithm configure(Properties configuration) throws JMException {
    Algorithm algorithm ;
    Mutation   mutation  ;

    HashMap  parameters ; // Operator parameters
    QualityIndicator indicators;
    // Creating the algorithm.
    algorithm = new PAES(problem_) ;

    // Algorithm parameters
    archiveSize_ = Integer.parseInt(configuration.getProperty("archiveSize",String.valueOf(archiveSize_)));
    maxEvaluations_  = Integer.parseInt(configuration.getProperty("maxEvaluations",String.valueOf(maxEvaluations_)));
    biSections_  = Integer.parseInt(configuration.getProperty("biSections",String.valueOf(biSections_)));
    algorithm.setInputParameter("maxEvaluations", maxEvaluations_);
    algorithm.setInputParameter("biSections", biSections_);
    algorithm.setInputParameter("archiveSize",archiveSize_ );


    mutationProbability_ = Double.parseDouble(configuration.getProperty("mutationProbability",String.valueOf(mutationProbability_)));
    mutationDistributionIndex_ = Double.parseDouble(configuration.getProperty("mutationDistributionIndex",String.valueOf(mutationDistributionIndex_)));
    parameters = new HashMap() ;
    parameters.put("probability", mutationProbability_) ;
    parameters.put("distributionIndex", mutationDistributionIndex_) ;
    mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);

    // Add the operators to the algorithm
    algorithm.addOperator("mutation",mutation);

     if((paretoFrontFile_!=null) &&(!paretoFrontFile_.equals(""))){
     indicators= new QualityIndicator(problem_,paretoFrontFile_);
     algorithm.setInputParameter("indicators",indicators);
    }

    return algorithm ;
  }
} // PAES_Settings