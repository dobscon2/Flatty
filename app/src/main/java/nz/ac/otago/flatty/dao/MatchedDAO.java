package nz.ac.otago.flatty.dao;

public class MatchedDAO {
    private String databaseUrl = "jdbc:mysql://13.239.27.21:3306/FlattyApp";
    private Boolean testMode = false;

    public MatchedDAO(){}

    public MatchedDAO(String databaseUrl, Boolean testMode){
        this.databaseUrl = databaseUrl;
        this.testMode = true;
    }

}
