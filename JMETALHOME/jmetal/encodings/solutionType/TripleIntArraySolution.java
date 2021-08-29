/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jmetal.encodings.solutionType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import jmetal.core.Problem;
import jmetal.core.SolutionType;
import jmetal.core.Variable;
import jmetal.encodings.variable.ArrayInt;

/**
 *
 * @author Mahshid
 */
public class TripleIntArraySolution extends SolutionType {
    private static List _cluster1 = Arrays.asList(0, 1, 3, 5);
    private static List _cluster2 = Arrays.asList(8,9, 11, 13);
    private static List _cluster3 =Arrays.asList(18,20, 22, 23, 25);
    public List allClusters;
    public int[] lowerBounds0;
    public int[] upperBounds0;
    public int[] lowerBounds1;
    public int[] upperBounds1;
    public int[] lowerBounds2;
    public int[] upperBounds2;
       
        public TripleIntArraySolution(Problem problem ) {
		super(problem);
		
	} // Constructor
        
        public Variable[] createVariables() throws ClassNotFoundException {
             Variable [] variables = new Variable[3];
             return variables;
         }
    public Variable[] createVariables(List WorkflowList) throws ClassNotFoundException {
		Variable [] variables = new Variable[3];
                
    lowerBounds0= new int[problem_.getNumberOfVariables()];
    upperBounds0= new int[problem_.getNumberOfVariables()];
    
    lowerBounds1= new int[problem_.getNumberOfVariables()];
    upperBounds1= new int[problem_.getNumberOfVariables()];
    
    lowerBounds2= new int[problem_.getNumberOfVariables()];
    upperBounds2= new int[problem_.getNumberOfVariables()];
    
    int index=0;
    for(int i=0; i<WorkflowList.size(); i++)
    {
        int levelLength= ((List)WorkflowList.get(i)).size();
        int lowerBound=index+1;
        int upperBound= index+levelLength;
        
     for(int j=0; j<levelLength; j++)
     {
         lowerBounds0[j+index]=lowerBound;
         upperBounds0[j+index]=upperBound;
     }
     index= index+levelLength;
        
    }
    
//    for (int i = 0; i < lowerBounds0.length; i++) {
//      lowerBounds0[i]=1;
//      upperBounds0[i]= problem_.getNumberOfVariables();
//    }
    
    allClusters= new LinkedList();
    allClusters.add(_cluster1);
    allClusters.add(_cluster2);
    allClusters.add(_cluster3);
    for (int i = 0; i < lowerBounds1.length; i++) {
      lowerBounds1[i]=1;
      upperBounds1[i]= allClusters.size();
    }
      
    variables[0] = new ArrayInt(problem_.getNumberOfVariables(),lowerBounds0, upperBounds0, false);
    variables[1] = new ArrayInt(problem_.getNumberOfVariables(),lowerBounds1, upperBounds1, true);
    
    for(int i=0; i<lowerBounds2.length; i++)
    {
        if(((ArrayInt)variables[1]).array_[i]==1)
        {    lowerBounds2[i]=1;
             upperBounds2[i]= _cluster1.size();
        }
        else if (((ArrayInt)variables[1]).array_[i]==2)
        {     
             lowerBounds2[i]=1;
             upperBounds2[i]= _cluster2.size();            
        }
        else if (((ArrayInt)variables[1]).array_[i]==3)
        {     
             lowerBounds2[i]=1;
             upperBounds2[i]= _cluster3.size();
        }
    }
            
    variables[2] = new ArrayInt(problem_.getNumberOfVariables(),lowerBounds2, upperBounds2);
      return variables ;
    
}
}
