package nz.ac.otago.flatty.dao;

import android.media.Image;
import android.os.Build;

import androidx.annotation.RequiresApi;

import nz.ac.otago.flatty.domain.Accounts;
import nz.ac.otago.flatty.domain.Details;

import java.sql.*;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class DetailsDAO {
    private String databaseUrl = "jdbc:mysql://13.239.27.21:3306/FlattyApp";
    private Boolean testMode = false;

    public DetailsDAO() {

    }

    public DetailsDAO(String databaseUrl) {
        this.databaseUrl = databaseUrl;
        this.testMode = true;
    }

    public void createDetails(Accounts account) {
        String sqlStatement1 = "INSERT into details (account_id, has_flat, degree, extra_info, weekly_budget, date_of_birth, diet_requirements) values (?,?,?,?,?,?,?)";
        String sqlStatement2 = "INSERT into details (account_id, has_flat, degree, extra_info, weekly_budget, date_of_birth) values (?,?,?,?,?,?)";
        Details detail = account.getDetails();

        try {
            String database_username = "createaccount";
            String database_password = "flatty2021";
            Connection conn = null;
            if (testMode) {
                conn = DbConnection.getTestConnection(databaseUrl, database_username, database_password);
            } else {
                conn = DbConnection.getDefaultConnection(database_username, database_password);
            }

            PreparedStatement stmt;
            if (detail.getDietRequirements() != null) {
                stmt = conn.prepareStatement(sqlStatement1);
            } else {
                stmt = conn.prepareStatement(sqlStatement2);
            }

            stmt.setInt(1, account.getAccountID());
            if (detail.getHasFlat()) {
                stmt.setString(2,"Y");
            } else {
                stmt.setString(2, "N");
            }
            stmt.setString(3, detail.getDegree());
            stmt.setString(4, detail.getExtraInfo());
            stmt.setString(5, detail.getWeeklyBudget());
            stmt.setDate(6, new Date(detail.getDateOfBirth().getTime()));

            if (detail.getDietRequirements() != null) {
                stmt.setString(7, detail.getDietRequirements());
            }

            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Details getDetails(Accounts account) {
        String sqlStatement = "SELECT * FROM details where account_id = ?";
        try {
            Connection conn = null;
            String database_username = "selectaccount";
            String database_password = "OtagoUni2021";
            if (testMode) {
                conn = DbConnection.getTestConnection(databaseUrl, database_username, database_password);
            } else {
                conn = DbConnection.getDefaultConnection(database_username, database_password);
            }
            PreparedStatement stmt = conn.prepareStatement(sqlStatement);

            stmt.setInt(1, account.getAccountID()); //fix getAccountID to retrieve generated accountID

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                int account_id = result.getInt("account_id");
                String hasFlat_result = result.getString("has_flat");
                String detail_degree = result.getString("degree");
                String account_extraInfo = result.getString("extra_info");
                String account_budget = result.getString("weekly_budget");
                Date account_date = result.getDate("date_of_birth");
                String account_diet_requirements = result.getString("diet_requirements");

                boolean hasFlat = true;

                if (hasFlat_result.equals("Y")) {
                    hasFlat = true;
                } else {
                    hasFlat = false;
                }

                Details details;
                if (account_diet_requirements != null) {
                    details = new Details(account_id, account_date, hasFlat, detail_degree, account_diet_requirements, account_extraInfo, account_budget);
                } else {
                    details = new Details(account_id, account_date, hasFlat, detail_degree, account_extraInfo, account_budget);
                }

                conn.close();
                return details;
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public void deleteDetails(Accounts account) {
        String sqlStatement = "DELETE from details where account_id = ?";
        try {
            Connection conn = null;
            String database_username = "deleteaccount";
            String database_password = "flattyproject";
            if (testMode) {
                conn = DbConnection.getTestConnection(databaseUrl, database_username, database_password);
            } else {
                conn = DbConnection.getDefaultConnection(database_username, database_password);
            }
            PreparedStatement stmt = conn.prepareStatement(sqlStatement);

            stmt.setInt(1, account.getAccountID()); //need to fix getAccountID

            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateDetails(Accounts account) {
        String sqlStatement1 = "UPDATE details set has_flat = ?, degree = ?, extra_info = ?, weekly_budget = ?, date_of_birth = ? where account_id = ?";
        String sqlStatement2 = "UPDATE details set has_flat = ?, degree = ?, extra_info = ?, weekly_budget = ?, date_of_birth = ?, diet_requirements = ? where account_id = ?";

        Details detail = account.getDetails();
        System.out.println("To updateDetails() method " + detail.toString());
        try {
            Connection conn = null;
            String database_username = "updateaccount";
            String database_password = "Info310Flatty";
            if (testMode) {
                conn = DbConnection.getTestConnection(databaseUrl, database_username, database_password);
            } else {
                conn = DbConnection.getDefaultConnection(database_username, database_password);
            }

            PreparedStatement stmt;
            if (detail.getDietRequirements() != null) {
                stmt = conn.prepareStatement(sqlStatement2);
            } else {
                stmt = conn.prepareStatement(sqlStatement1);
            }

            if (detail.getHasFlat()) {
                stmt.setString(1, "Y");
            } else {
                stmt.setString(1, "N");
            }

            stmt.setString(2, detail.getDegree());
            stmt.setString(3, detail.getExtraInfo());
            stmt.setString(4, detail.getWeeklyBudget());
            stmt.setDate(5, new Date(detail.getDateOfBirth().getTime()));

            if (detail.getDietRequirements() != null) {
                stmt.setString(6, detail.getDietRequirements());
                stmt.setInt(7, account.getAccountID());
            } else {
                stmt.setInt(6, account.getAccountID());
            }
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /* Matchmaking Method based on keywords in the login user's extraInfo variable*/
    public ArrayList<Accounts> match(Accounts account) {
        ArrayList<Accounts> matchmaking = new ArrayList<Accounts>();
        String account_info = account.getDetails().getExtraInfo();
        String tags[] = account_info.split(", ");

        StringBuilder sqlStatement = new StringBuilder("SELECT * FROM details WHERE");

        for (int i = 0; i < tags.length; i++) {
            if (i == 0) {
                sqlStatement.append(" extra_info like '%" + tags[i] + "%'");
            } else {
                sqlStatement.append(" OR extra_info like '%" + tags[i] + "%'");
            }
        }

        try {
            Connection conn = null;
            String database_username = "selectaccount";
            String database_password = "OtagoUni2021";
            if (testMode) {
                conn = DbConnection.getTestConnection(databaseUrl, database_username, database_password);
            } else {
                conn = DbConnection.getDefaultConnection(database_username, database_password);
            }

            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement.toString());

            while (result.next()) {
                Accounts profileAccount = null;
                Details profileDetails = null;
                int account_id = result.getInt("account_id");
                boolean hasFlat = true;

                if (result.getString("has_flat").equals("Y")) {
                    hasFlat = true;
                } else {
                    hasFlat = false;
                }

                String degree = result.getString("degree");
                String weeklyBudget = result.getString("weekly_budget");
                String extraInfo = result.getString("extra_info");
                Date dateOfBirth = result.getDate("date_of_birth");
                String diet_requirement = result.getString("diet_requirements");

                profileDetails = new Details(account_id, dateOfBirth, hasFlat, degree, diet_requirement, extraInfo, weeklyBudget);
                System.out.println(profileDetails.toString());

                String accountSQL = "SELECT * FROM accounts WHERE account_id = '" + account_id + "'";
                Statement stmtAccount = conn.createStatement();
                ResultSet accountResult = stmtAccount.executeQuery(accountSQL);

                if (accountResult.next()) {
                    String email = accountResult.getString("email");
                    String password = accountResult.getString("password");
                    String firstName = accountResult.getString("firstname");
                    String lastName = accountResult.getString("lastname");
                    Blob photoBlob = accountResult.getBlob("photo");
                    byte[] photo = photoBlob.getBytes(1, (int) photoBlob.length());

                    Boolean isActive = true;

                    if(accountResult.getString("is_active").equals("Y")) {
                        isActive = true;
                    } else {
                        isActive = false;
                    }

                    profileAccount = new Accounts(account_id, firstName, lastName, email, password, photo);
                }
                profileAccount.setDetails(profileDetails);
                matchmaking.add(profileAccount);
            }
            conn.close();
            return matchmaking;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}




