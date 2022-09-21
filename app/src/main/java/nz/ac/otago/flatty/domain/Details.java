package nz.ac.otago.flatty.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Details implements Serializable {
    private Integer accountID;
    private Date dateOfBirth;
    private boolean hasFlat;
    private String degree;
    private String dietRequirements;
    private String extraInfo;
    private String weeklyBudget;

    /* User could not have dietRequirements */
    public Details(Integer accountID, Date dateOfBirth, boolean hasFlat, String degree, String extraInfo, String weeklyBudget) {
        this.accountID = accountID;
        this.dateOfBirth = dateOfBirth;
        this.hasFlat = hasFlat;
        this.degree = degree;
        this.extraInfo = extraInfo;
        this.weeklyBudget = weeklyBudget;
    }
    public Details(){}

    public Details(Integer accountID, Date dateOfBirth, boolean hasFlat, String degree, String dietRequirements, String extraInfo, String weeklyBudget) {
        this.accountID = accountID;
        this.dateOfBirth = dateOfBirth;
        this.hasFlat = hasFlat;
        this.degree = degree;
        this.dietRequirements = dietRequirements;
        this.extraInfo = extraInfo;
        this.weeklyBudget = weeklyBudget;
    }

    public Integer getAccountID() {

        return accountID;
    }

    public Date getDateOfBirth() {

        return dateOfBirth;
    }

    public boolean getHasFlat() {

        return hasFlat;
    }

    public String getDegree() {

        return degree;
    }

    public String getDietRequirements() {

        return dietRequirements;
    }

    public String getExtraInfo() {

        return extraInfo;
    }

    public String getWeeklyBudget() {

        return weeklyBudget;
    }

    public void setDateOfBirth(Date dateOfBirth) {

        this.dateOfBirth = dateOfBirth;
    }

    public void setHasFlat(boolean hasFlat) {

        this.hasFlat = hasFlat;
    }

    public void setDegree(String degree) {

        this.degree = degree;
    }

    public void setDietRequirements(String dietRequirements) {

        this.dietRequirements = dietRequirements;
    }

    public void setExtraInfo(String extraInfo) {

        this.extraInfo = extraInfo;
    }

    public void setWeeklyBudget(String weeklyBudget) {

        this.weeklyBudget = weeklyBudget;
    }

    @Override
    public String toString() {
        return "Details{" +
                "accountID=" + accountID +
                ", dateOfBirth=" + dateOfBirth +
                ", hasFlat=" + hasFlat +
                ", degree='" + degree + '\'' +
                ", dietRequirements='" + dietRequirements + '\'' +
                ", extraInfo='" + extraInfo + '\'' +
                ", weeklyBudget=" + weeklyBudget +
                '}';
    }
}