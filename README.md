# Evolutionary Computation -assisted performance testing
 
This repository contains source code for an evolutionary search-based performance (load) testing approach that is able to generate efficient performance test cases.
This technique can be considered a genetic-based load testing approach that investigates the effects of different loads (number of virtual users) on the transactions. In this evolutionary computation-assisted performance testing technique, we are using several metaheuristics,
multi-objective evolutionary algorithms (MOEAs) to generate the performance tests.

The used algorithms: 

* **NSGA-II:** Non-dominated Storing Genetic Algorithm II
* **PAES:** Pareto Archived Evolution Strategy
* **SPEA2:** The Strength Pareto Evolutionary Algorithm 2
* **MOCell:** Multi-Objective Cellular Genetic Algorithm
* **Random Search**

All these algorithms are implemented and executed using jMetal 4.5 framework. The implementation of the MOEAs can be found following *./JMETALHOME/jmetal/experiments/studies* folders while the problem formulation file can be found in *problems* folder, with the name *LoadTesting.java*.
Each of these algorithms can be run/executed independently. To record (transactions) and execute the performance tests, we are using Appache JMeter 3.1.

More information about the structure and procedure of this technique is available at: http://urn.kb.se/resolve?urn=urn:nbn:se:mdh:diva-55931
