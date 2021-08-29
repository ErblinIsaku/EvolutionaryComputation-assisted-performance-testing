package jmetal.Qlearning_Ramdom_Dummy;

import java.util.HashMap;

public class SUTstate {
    public  QualityMeasures qualityMeasures;
    public  int workload;
    public Transaction[] transactions;
    public SUTstate(QualityMeasures qualityMeasures, int workLoad, Transaction[] transactions) {
        this.qualityMeasures = qualityMeasures;
        this.workload = workLoad;
        this.transactions = transactions;
    }


    @Override
    public String toString(){
       String qms = "STATE QUALITY MEASURES:\n" + qualityMeasures.toString();
       String twls = "TOTAL WORKLOAD: " + workload;
       String ts = "";
       for(Transaction t: transactions){
           ts = ts+t.toString()+",";
       }
       String seperator = "__________________________________________\n";

       return qms+"\n"+twls+"\n"+ts+"\n"+seperator;
    }


}
