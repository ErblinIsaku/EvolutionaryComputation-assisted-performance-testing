package jmetal.Qlearning_Ramdom_Dummy;

public class Transaction {
    public String name;
    public int workLoad;
    public int workload;

    //for seperate transactions
    //protected QualityMeasures qualityMeasures = new QualityMeasures();

    public Transaction(String name, int workLoad) {
        this.name = name;
        this.workLoad = workLoad;
    }
    @Override
    public String toString(){
        return this.name+": "+workLoad;
    }
}
