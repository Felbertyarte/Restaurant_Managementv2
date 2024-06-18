package restaurant.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import restaurant.db.database;

public class user_model extends database {
    private PreparedStatement ps;

    public ResultSet login(String username, String password) throws SQLException {

        ps = getConnection().prepareStatement(
                "SELECT " +
                        "tbl_usertype.usertype as usertype, " +
                        "tbl_store.name as storename, " +
                        "personID, " +
                        "usertypeID, " +
                        "storeID " +
                        "FROM tbl_user " +
                        "INNER JOIN tbl_usertype ON tbl_user.usertypeID = tbl_usertype.ID " +
                        "INNER JOIN tbl_store ON tbl_user.storeID = tbl_store.ID " +
                        "WHERE username =? AND password =?");
        ps.setString(1, username);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();
        return rs;

        // how to user
        // auth_model am = new auth_model();

        // ResultSet un = am.loginsuccessful("felbert", "felbert14");

        // if (un.next()) {
        // System.out.println(un.getString("usertype"));
        // } else {
        // System.out.println("failed");
        // }

    }

    public int register_personal_info(String name, String address, String email, String contact) throws SQLException {
        try {
            // Validate input values
            if (name.trim().isEmpty()
                    || contact.trim().isEmpty()) {
                // Handle empty or whitespace-only values (you can throw an exception or log a
                // message)
                System.err.println("Error: Empty or whitespace-only input detected.");
                return 0;
            }

            ps = getConnection().prepareStatement(
                    "INSERT INTO tbl_person (name, address, Email, Contact) VALUES (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS); // Explicitly request generated keys
            ps.setString(1, name);
            ps.setString(2, "none");
            ps.setString(3, "none");
            ps.setString(4, contact);
            ps.executeUpdate(); // Execute the INSERT statement

            // Retrieve the generated ID
            ResultSet result = ps.getGeneratedKeys();
            if (result.next()) {
                return result.getInt(1);
            }
            getConnection().close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            ps = null;
            getConnection().close();
            ps.close();
            return 0;
        }
        ps = null;
        return 0;
        // register_model person_info_reg = new register_model();
        // if return 0 possible not fill all the field
        // int rs = person_info_reg.register_personal("", "", "", "");
        // System.out.println(rs);
    }

    // give me sugest name this function
    public void register_user(String username, String password, int personID, int usertypeID, int storeID)
            throws SQLException {
        try {
            // Validate input values
            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                // Handle empty or whitespace-only values (you can throw an exception or log a
                // message)
                System.err.println("Error: Empty or whitespace-only input detected.");
            }

            ps = getConnection().prepareStatement(
                    "INSERT INTO tbl_user (username, password, personID, usertypeID, storeID) VALUES (?,?,?,?,?)"); // Explicitly
                                                                                                                    // request
                                                                                                                    // generated                                                                                                        // keys
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setInt(3, personID);
            ps.setInt(4, usertypeID);
            ps.setInt(5, storeID);
            ps.executeUpdate();
            // Execute the INSERT statement
            getConnection().close();
            ps.close();
        } catch (SQLException e) {
            getConnection().close();
            ps.close();
            e.printStackTrace();
        }
            
            
    }
    public ObservableList<user_person> get_user_person() throws SQLException{
        String sql = "select tbl_user.ID as userID, tbl_user.username, tbl_user.password, tbl_usertype.usertype, tbl_store.name as store_name, tbl_user.personID, tbl_person.name as person_name, tbl_person.Contact, tbl_user.usertypeID,tbl_user.storeID from tbl_user INNER JOIN tbl_store on tbl_user.storeID = tbl_store.ID INNER JOIN tbl_usertype on tbl_user.usertypeID = tbl_usertype.ID INNER JOIN tbl_person on tbl_user.personID = tbl_person.ID";
        PreparedStatement pst = getConnection().prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        ObservableList<user_person> list = FXCollections.observableArrayList();
        while (rs.next()) {
            user_person UserPerson = new user_person();
            UserPerson.setStorename(rs.getString("store_name"));
            UserPerson.setusername(rs.getString("username"));
            UserPerson.setpassword(rs.getString("password"));
            UserPerson.setusertype(rs.getString("usertype"));
            UserPerson.setname(rs.getString("person_name"));
            UserPerson.setcontact(rs.getString("Contact"));
            list.add(UserPerson);
            
        }
        return list;
    }
    public ObservableList<user_type> get_user_type() throws SQLException{
        String sql = "select * from tbl_usertype";
        PreparedStatement pst = getConnection().prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        ObservableList<user_type> list = FXCollections.observableArrayList();
        while (rs.next()) {
            user_type UserType = new user_type();
            UserType.set_user_type(rs.getString("usertype"));
            UserType.set_ID(rs.getInt("ID"));
            list.add(UserType);
            
        }
        return list;
    }
}