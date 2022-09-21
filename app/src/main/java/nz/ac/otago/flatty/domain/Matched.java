package nz.ac.otago.flatty.domain;

import java.io.Serializable;

public class Matched implements Serializable {
    private Integer accountID;
    private String firstname;
    private String lastName;

    public Matched(){}

    public Matched(Integer accountID, String firstname, String lastName){
        this.accountID = accountID;
        this.firstname = firstname;
        this.lastName = lastName;
    }


}
