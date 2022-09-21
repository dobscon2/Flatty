package nz.ac.otago.flatty.dao;

import nz.ac.otago.flatty.domain.Accounts;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashSet;

import android.graphics.Bitmap;
import android.media.Image;

public class AccountsDAO {

    private String databaseUrl = "jdbc:mysql://13.239.27.21:3306/FlattyApp";
    private Boolean testMode = false;

    public AccountsDAO() {

    }

    public AccountsDAO(String databaseUrl) {
        this.databaseUrl = databaseUrl;
        this.testMode = true;
    }

    public void createAccount(Accounts account) {
        String sqlStatement = "INSERT into accounts (email, password, firstname, lastname, is_active) values (?, SHA(?), ?, ?, ?)";
        String sqlStatementTest = "INSERT into accounts (email, password, firstname, lastname, is_active) values (?, ?, ?, ?, ?)";

        try {
            String database_username = "createaccount";
            String database_password = "flatty2021";
            Connection conn = null;
            PreparedStatement stmt = null;
            if (testMode) {
                conn = DbConnection.getTestConnection(databaseUrl, database_username, database_password);
                stmt = conn.prepareStatement(sqlStatementTest);
            } else {
                conn = DbConnection.getDefaultConnection(database_username, database_password);
                stmt = conn.prepareStatement(sqlStatement);
            }

            stmt.setString(1, account.getEmail());
            stmt.setString(2, account.getPassword());
            stmt.setString(3, account.getFirstName());
            stmt.setString(4, account.getLastName());
            stmt.setString(5, "Y");

            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Boolean validateLogin(String email, String password) {
        String sqlStatement = "SELECT ACCOUNT_ID FROM accounts WHERE email = ? && password = SHA(?)";
        String sqlStatementTest = "SELECT ACCOUNT_ID FROM accounts WHERE email = ? and password = ?";

        try {
            Connection conn = null;
            String database_username = "selectaccount";
            String database_password = "OtagoUni2021";
            PreparedStatement stmt = null;
            if (testMode) {
                conn = DbConnection.getTestConnection(databaseUrl, database_username, database_password);
                stmt = conn.prepareStatement(sqlStatementTest);
            } else {
                conn = DbConnection.getDefaultConnection(database_username, database_password);
                stmt = conn.prepareStatement(sqlStatement);
            }

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                conn.close();
                return true;
            } else {
                conn.close();
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Accounts getAccount(String email) {
        String sqlStatement = "SELECT * FROM accounts where email = ?";

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

            stmt.setString(1, email);

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                int account_id = result.getInt("account_id");
                String account_email = result.getString("email");
                String account_password = result.getString("password");
                String account_firstname = result.getString("firstname");
                String account_lastname = result.getString("lastname");
                Blob account_photo = result.getBlob("photo");

                Accounts account = null;

                if (account_photo == null) {
                    // create account object without photo
                    account = new Accounts(account_id, account_firstname, account_lastname, account_email, account_password);
                } else {
                    // convert blob to image
                    byte[] converted_photo = account_photo.getBytes(1, (int) account_photo.length());
                    account = new Accounts(account_id, account_firstname, account_lastname, account_email, account_password, converted_photo);
                }


                conn.close();
                return account;
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void updateAccount(Accounts account) {
        System.out.println("executed");
        String sqlStatement1 = "UPDATE accounts set email = ?, firstname = ?, lastname = ? where account_id = ?";
        String sqlStatement2 = "UPDATE accounts set email = ?, firstname = ?, lastname = ?, photo = ? where account_id = ?"; // update photo

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

            if (account.getPhoto() != null) {
                stmt = conn.prepareStatement(sqlStatement1);
            } else {
                stmt = conn.prepareStatement(sqlStatement1);
            }

            stmt.setString(1, account.getEmail());
            stmt.setString(2, account.getFirstName());
            stmt.setString(3, account.getLastName());

            /*if (account.getPhoto() != null) {
                byte[] photo = account.getPhoto();
                //stmt.setBlob(4, new ByteArrayInputStream(photo));
                stmt.setInt(5, account.getAccountID());
            } else { */
            stmt.setInt(4, account.getAccountID());
            // }

            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeAccount(Accounts account) {
        String sqlStatement = "DELETE from accounts where account_id = ?";

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

            stmt.setInt(1, account.getAccountID());

            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMatch(Accounts account) {
        String sqlStatement = "UPDATE accounts set matches = ? where account_id = ?";

        StringBuilder matchBuilder = new StringBuilder();
        HashSet<Integer> matches = account.getMatches();
        int count = 1;

        for (int matchID : matches) {
            if (count != matches.size()) {
                matchBuilder.append(matchID + ", ");
                count++;
            } else {
                matchBuilder.append(matchID);
            }
        }

        try {
            Connection conn = null;
            String database_username = "updateaccount";
            String database_password = "Info310Flatty";
            if (testMode) {
                conn = DbConnection.getTestConnection(databaseUrl, database_username, database_password);
            } else {
                conn = DbConnection.getDefaultConnection(database_username, database_password);
            }

            PreparedStatement stmt = conn.prepareStatement(sqlStatement);

            stmt.setString(1, matchBuilder.toString());
            stmt.setInt(2, account.getAccountID());
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }

    }

    public HashSet<Integer> getMatches(Accounts account) {
        String sqlStatement = "SELECT matches from accounts where account_id = ?";

        HashSet<Integer> matchResult = new HashSet<>();

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

            stmt.setInt(1, account.getAccountID());

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                String matchString = result.getString("matches");
                if (matchString != null) {
                    String[] matchSplit = matchString.split(", ");

                    for (String match : matchSplit) {
                        matchResult.add(Integer.parseInt(match));
                    }
                }
                conn.close();
                return matchResult;
            }
            conn.close();
            return null;

        } catch (SQLException ex) {
            ex.getMessage();
        }
        return null;
    }

    public Accounts getAccount(int searchID) {
        String sqlStatement = "SELECT * FROM accounts where account_id = ?";

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

            stmt.setInt(1, searchID);

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                int account_id = result.getInt("account_id");
                String account_email = result.getString("email");
                String account_password = result.getString("password");
                String account_firstname = result.getString("firstname");
                String account_lastname = result.getString("lastname");
                Blob account_photo = result.getBlob("photo");

                Accounts account = null;

                if (account_photo == null) {
                    // create account object without photo
                    account = new Accounts(account_id, account_firstname, account_lastname, account_email, account_password);
                } else {
                    // convert blob to image
                    byte[] converted_photo = account_photo.getBytes(1, (int) account_photo.length());
                    account = new Accounts(account_id, account_firstname, account_lastname, account_email, account_password, converted_photo);
                }


                conn.close();
                return account;
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}