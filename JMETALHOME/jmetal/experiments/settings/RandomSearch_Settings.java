//  RandomSearch_Settings.java
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
import jmetal.metaheuristics.randomSearch.RandomSearch;
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
 * Settings class of algorithm RandomSearch
 */
public class RandomSearch_Settings extends Settings {
  // Default experiments.settings
  public int maxEvaluations_ = 100;

  /**
   * Constructor
   * @param problem Problem to solve
   */
  public RandomSearch_Settings(String problem) {
    super(problem);

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




//    Object [] problemParams = {"Real"};
//    try {
//	    problem_ = (new ProblemFactory()).getProblem(problemName_, problemParams);
//    } catch (JMException e) {
//	    e.printStackTrace();
//    }
  } // RandomSearch_Settings

  /**
   * Configure the random search algorithm with default parameter experiments.settings
   * @return an algorithm object
   * @throws jmetal.util.JMException
   */
  public Algorithm configure() throws JMException {
    Algorithm algorithm;
    QualityIndicator indicators;
    // Creating the problem
    algorithm = new RandomSearch(problem_);

    // Algorithm parameters
    algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

     if((paretoFrontFile_!=null) &&(!paretoFrontFile_.equals(""))){
     indicators= new QualityIndicator(problem_,paretoFrontFile_);
     algorithm.setInputParameter("indicators",indicators);
    }

    return algorithm;
  } // Constructor

  /**
   * Configure SMPSO with user-defined parameter experiments.settings
   * @return A SMPSO algorithm object
   */
  @Override
  public Algorithm configure(Properties configuration) throws JMException {
    Algorithm algorithm ;
    QualityIndicator indicators;
    HashMap parameters ; // Operator parameters

    // Creating the algorithm.
    algorithm = new RandomSearch(problem_) ;

    // Algorithm parameters
    maxEvaluations_  = Integer.parseInt(configuration.getProperty("maxEvaluations",String.valueOf(maxEvaluations_)));

    algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

    if((paretoFrontFile_!=null) &&(!paretoFrontFile_.equals(""))){
     indicators= new QualityIndicator(problem_,paretoFrontFile_);
     algorithm.setInputParameter("indicators",indicators);
    }

    return algorithm ;
  }
} // RandomSearch_Settings
