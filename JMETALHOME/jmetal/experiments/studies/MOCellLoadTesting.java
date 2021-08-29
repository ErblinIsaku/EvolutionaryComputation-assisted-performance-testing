/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jmetal.experiments.studies;

import jmetal.core.Algorithm;
import jmetal.experiments.Experiment;
import jmetal.experiments.Settings;
import jmetal.experiments.settings.MOCell_Settings;
import jmetal.experiments.settings.PAES_Settings;
import jmetal.experiments.util.Friedman;
import jmetal.util.JMException;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erblin
 */
public class MOCellLoadTesting extends Experiment{
    @Override
    public void algorithmSettings(String problemName, int problemIndex, Algorithm[] algorithm) throws ClassNotFoundException
    {
        try {
            int numberOfAlgorithms = algorithmNameList_.length ;
            HashMap[] parameters = new HashMap[numberOfAlgorithms];
            for ( int i = 0 ; i < numberOfAlgorithms ; i++) {
                parameters [ i ] = new HashMap ( ) ;
            }

            if (!(paretoFrontFile_[problemIndex] == null) && !paretoFrontFile_[problemIndex].equals("")) {
                for (int i = 0; i < numberOfAlgorithms; i++)
                    parameters[i].put("paretoFrontFile_", paretoFrontDirectory_+"/"+paretoFrontFile_[problemIndex]);
            }



            algorithm[0] = new MOCell_Settings(problemName).configure(parameters[0]);

        }
        catch(IllegalArgumentException ex){
            Logger.getLogger(MOCellLoadTesting.class.getName()).log(Level.SEVERE, null,ex );
        }
        catch (JMException ex){
            Logger.getLogger(MOCellLoadTesting.class.getName()).log(Level.SEVERE, null,ex );
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MOCellLoadTesting.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main ( String [ ] args ) throws JMException , IOException {

        MOCellLoadTesting exp = new MOCellLoadTesting( );
        exp.experimentName_ = "MOCellLoadTesting";

        exp.algorithmNameList_ = new String[ ] {"MOCell"} ;
//
        exp.problemList_ = new String[ ]{"LoadTesting"};
        exp.paretoFrontFile_ = new String[1];
        exp.indicatorList_ = new String [ ] { "HV" , "SPREAD" , "EPSILON" } ;
        int numberOfAlgorithms= exp.algorithmNameList_.length;
        exp.experimentBaseDirectory_="/Users/rise/Desktop/paretoFiles/"+exp.experimentName_;
        exp.paretoFrontDirectory_ = "" ;
//        exp.paretoFrontDirectory_ = "/Users/rise/Desktop/paretoFiles/NSGAIILoadTesting/referenceFronts" ;


        exp.algorithmSettings_= new Settings[numberOfAlgorithms];
        exp.independentRuns_=5;

        exp.initExperiment();

        // Run the experiments
        int numberOfThreads ;
        exp.runExperiment(1) ;
        exp.generateQualityIndicators() ;

        // Generate latex tables (comment this sentence is not desired)
        exp.generateLatexTables() ;


        // Configure the R scripts to be generated
    int rows  ;
    int columns  ;
    String prefix ;
    String [] problems ;
    boolean notch ;

    // Configuring scripts for LoadTesting
    rows = 1 ;
    columns = 1 ;
    prefix = "LoadTesting";
    problems = new String[]{"LoadTesting"} ;

    exp.generateRBoxplotScripts(rows, columns, problems, prefix, notch = true, exp) ;
    exp.generateRWilcoxonScripts(problems, prefix, exp) ;


    // Applying Friedman test
    Friedman test = new Friedman(exp);
    test.executeTest("EPSILON");
    test.executeTest("HV");
    test.executeTest("SPREAD");


    }

}
