//  BitFlipMutation.java
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

package jmetal.operators.mutation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.solutionType.TripleIntArraySolution;
import jmetal.encodings.variable.ArrayInt;
import jmetal.encodings.variable.Binary;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 * This class implements a bit flip mutation operator.
 * NOTE: the operator is applied to binary or integer solutions, considering the
 * whole solution as a single encodings.variable.
 */
public class TripleIntArrayMutation extends Mutation {
  /**
   * Valid solution types to apply this operator 
   */
  private static final List VALID_TYPES = Arrays.asList(TripleIntArraySolution.class);
      

  private Double mutationProbability_ = null ;
  
	/**
	 * Constructor
	 * Creates a new instance of the Bit Flip mutation operator
	 */
	public TripleIntArrayMutation(HashMap<String, Object> parameters) {
		super(parameters) ;
  	if (parameters.get("probability") != null)
  		mutationProbability_ = (Double) parameters.get("probability") ;  		
	} // BitFlipMutation

	/**
	 * Perform the mutation operation
	 * @param probability Mutation probability
	 * @param solution The solution to mutate
	 * @throws JMException
	 */
	public void doMutation(double probability, Solution solution) throws JMException {
		try {
			if ((solution.getType().getClass() == TripleIntArraySolution.class)) {
                            Variable[] variablesOfSolution = solution.getDecisionVariables();
                            ArrayInt Variable1 = (ArrayInt)variablesOfSolution[1];
                            Boolean MutationType;
                            if (PseudoRandom.randDouble(0, 1)<0.5)
                                MutationType=true;
                            else 
                                MutationType= false;
                            if (MutationType==true){
				for (int i = 0; i < ((ArrayInt)solution.getDecisionVariables()[1]).getLength(); i++) {
					if (PseudoRandom.randDouble() < probability) {
							int value = PseudoRandom.randInt(
                                                               ((TripleIntArraySolution) solution.getType()).lowerBounds1[i],((TripleIntArraySolution) solution.getType()).upperBounds1[i]);
						        ((ArrayInt)solution.getDecisionVariables()[1]).setValue(i, value);
                                                        for (int j=0; j< ((TripleIntArraySolution)solution.getType()).allClusters.size();j++)
                                                        {
                                                            if (((ArrayInt)solution.getDecisionVariables()[1]).getValue(i)==j+1)
                                                            {int value1 = PseudoRandom.randInt(1,((List)((TripleIntArraySolution)solution.getType()).allClusters.get(j)).size());
  
                                                            
                                                            ((ArrayInt)solution.getDecisionVariables()[2]).setValue(i, value1);
                                                            break;
                                                            }
                                                        }
						}
					
				}
                            }
                            else {
                                
                                if (PseudoRandom.randDouble() < probability) {
                                int pos1 ;
                                int pos2 ;
                                int length= ((ArrayInt)solution.getDecisionVariables()[1]).getLength();
                                pos1 = PseudoRandom.randInt(0,length-1) ;
                                pos2 = PseudoRandom.randInt(0,length-1) ;

                                while (pos1 == pos2) {
                                  if (pos1 == (length - 1)) 
                                    pos2 = PseudoRandom.randInt(0, length- 2);
                                  else 
                                    pos2 = PseudoRandom.randInt(pos1, length- 1);
                                } // while
                                // swap
                                int pos1Value = ((ArrayInt)solution.getDecisionVariables()[1]).getValue(pos1);
                                int pos2Value= ((ArrayInt)solution.getDecisionVariables()[1]).getValue(pos2);
                                ((ArrayInt)solution.getDecisionVariables()[1]).setValue(pos1, pos2Value);
                                ((ArrayInt)solution.getDecisionVariables()[1]).setValue(pos2, pos1Value);
                                
                                int pos1ValuePrim = ((ArrayInt)solution.getDecisionVariables()[2]).getValue(pos1);
                                int pos2ValuePrim= ((ArrayInt)solution.getDecisionVariables()[2]).getValue(pos2);
                                ((ArrayInt)solution.getDecisionVariables()[2]).setValue(pos1, pos2ValuePrim);
                                ((ArrayInt)solution.getDecisionVariables()[2]).setValue(pos2, pos1ValuePrim);
                                  
                              } // if
                                
                                
                                
                            }

				
			} // if
			else { 
                            Configuration.logger_.severe("TripleIntArrayMutation.doMutation: " +
					"ClassCastException error , invalidType" );
                            Class cls = java.lang.String.class;
                            String name = cls.getName();
                            throw new JMException("Exception in " + name + ".doMutation()");
                            
			} // else
		} catch (ClassCastException e1) {
			Configuration.logger_.severe("TripleIntArrayMutation.doMutation: " +
					"ClassCastException error" + e1.getMessage());
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".doMutation()");
		}
	} // doMutation

	/**
	 * Executes the operation
	 * @param object An object containing a solution to mutate
	 * @return An object containing the mutated solution
	 * @throws JMException 
	 */
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution) object;

		if (!VALID_TYPES.contains(solution.getType().getClass())) {
			Configuration.logger_.severe("TripleIntArrayMutation.execute: the solution " +
					"is not of the right type." + solution.getType() + " is obtained");

			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		} // if 

		doMutation(mutationProbability_, solution);
		return solution;
	} // execute
} // BitFlipMutation
