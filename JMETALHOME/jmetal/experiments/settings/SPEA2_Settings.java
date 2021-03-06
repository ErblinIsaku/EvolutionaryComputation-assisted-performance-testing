//  SPEA2_Settings.java
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.experiments.Settings;
import jmetal.metaheuristics.spea2.SPEA2;
import jmetal.operators.crossover.Crossover;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.Selection;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.LoadTesting;
import jmetal.problems.ProblemFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

/**
 * Settings class of algorithm SPEA2
 */
public class SPEA2_Settings extends Settings {

  public int populationSize_           ;
  public int archiveSize_              ;
  public int maxEvaluations_           ;
  public double mutationProbability_   ;
  public double crossoverProbability_  ;
  public double crossoverDistributionIndex_ ;
  public double mutationDistributionIndex_  ;

  /**
   * Constructor
     * @param problem
   */
  public SPEA2_Settings(String problem) {
    super(problem) ;

//    Object [] problemParams = {"Real"};
//    try {
//	    problem_ = (new ProblemFactory()).getProblem(problemName_, problemParams);
//    } catch (JMException e) {
//	    // TODO Auto-generated catch block
//	    e.printStackTrace();
//    }

      problem_ = new LoadTesting("IntSolutionType", 2);


    populationSize_           = 50   ;
    archiveSize_              = 50   ;
    maxEvaluations_           = 20 ;
    mutationProbability_   = 0.4 ;
    crossoverProbability_  = 0.9   ;
    crossoverDistributionIndex_ = 20.0  ;
    mutationDistributionIndex_  = 20.0  ;

  } // SPEA2_Settings

  /**
   * Configure SPEA2 with default parameter experiments.settings
   * @return an algorithm object
   * @throws jmetal.util.JMException
   */
  public Algorithm configure() throws JMException {
    Algorithm algorithm ;
    Operator  crossover ;         // Crossover operator
    Operator  mutation  ;         // Mutation operator
    Operator  selection ;         // Selection operator

    HashMap  parameters ; // Operator parameters
    QualityIndicator indicators;
    // Creating the problem
    algorithm = new SPEA2(problem_) ;

    // Algorithm parameters
    algorithm.setInputParameter("populationSize", populationSize_);
    algorithm.setInputParameter("archiveSize", archiveSize_);
    algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

    // Mutation and Crossover for Real codification
    parameters = new HashMap() ;
    parameters.put("probability", crossoverProbability_) ;
    parameters.put("distributionIndex", crossoverDistributionIndex_) ;
//    crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters);
    crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters);

    parameters = new HashMap() ;
    parameters.put("probability", mutationProbability_) ;
    parameters.put("distributionIndex", mutationDistributionIndex_) ;
//    mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);
    mutation = MutationFactory.getMutationOperator("PolynomialBitFlipMutation", parameters);

    // Selection operator
    parameters = null ;
//    selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters) ;
    selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters) ;


    // Add the operators to the algorithm
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("mutation",mutation);
    algorithm.addOperator("selection",selection);

    if((paretoFrontFile_!=null) &&(!paretoFrontFile_.equals(""))){
     indicators= new QualityIndicator(problem_,paretoFrontFile_);
     algorithm.setInputParameter("indicators",indicators);
    }

   return algorithm ;
  } // configure

  /**
   * Configure SPEA2 with user-defined parameter experiments.settings
   * @return A SPEA2 algorithm object
   */
  @Override
  public Algorithm configure(Properties configuration) throws JMException {
    Algorithm algorithm ;
    Selection selection ;
    Crossover crossover ;
    Mutation mutation  ;

    HashMap  parameters ; // Operator parameters
     QualityIndicator indicators;
    // Creating the algorithm.
    algorithm = new SPEA2(problem_) ;

    // Algorithm parameters
    populationSize_ = Integer.parseInt(configuration.getProperty("populationSize",String.valueOf(populationSize_)));
    maxEvaluations_  = Integer.parseInt(configuration.getProperty("maxEvaluations",String.valueOf(maxEvaluations_)));
    archiveSize_  = Integer.parseInt(configuration.getProperty("archiveSize",String.valueOf(archiveSize_)));
    algorithm.setInputParameter("populationSize",populationSize_);
    algorithm.setInputParameter("maxEvaluations",maxEvaluations_);
    algorithm.setInputParameter("archiveSize", archiveSize_);

    // Mutation and Crossover for Real codification
    crossoverProbability_ = Double.parseDouble(configuration.getProperty("crossoverProbability",String.valueOf(crossoverProbability_)));
    crossoverDistributionIndex_ = Double.parseDouble(configuration.getProperty("crossoverDistributionIndex",String.valueOf(crossoverDistributionIndex_)));
    parameters = new HashMap() ;
    parameters.put("probability", crossoverProbability_) ;
    parameters.put("distributionIndex", crossoverDistributionIndex_) ;
    crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters);

    mutationProbability_ = Double.parseDouble(configuration.getProperty("mutationProbability",String.valueOf(mutationProbability_)));
    mutationDistributionIndex_ = Double.parseDouble(configuration.getProperty("mutationDistributionIndex",String.valueOf(mutationDistributionIndex_)));
    parameters = new HashMap() ;
    parameters.put("probability", mutationProbability_) ;
    parameters.put("distributionIndex", mutationDistributionIndex_) ;
    mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);

    // Selection Operator
    parameters = null ;
    selection = SelectionFactory.getSelectionOperator("RandomSelection", parameters) ;

    // Add the operators to the algorithm
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("mutation",mutation);
    algorithm.addOperator("selection",selection);

    if((paretoFrontFile_!=null) &&(!paretoFrontFile_.equals(""))){
     indicators= new QualityIndicator(problem_,paretoFrontFile_);
     algorithm.setInputParameter("indicators",indicators);
    }

    return algorithm ;
  }
} // SPEA2_Settings
