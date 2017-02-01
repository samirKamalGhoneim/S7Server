package DataTransferObject;

import ServerPackage.ContactList;
import ServerPackage.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author samir
 */
public class DatabaseHandler {

    private String url;
    private String username;
    private String password;
    private Connection con;

    /**
     * *************************Constructors**************************************
     */
    public DatabaseHandler() {
        this.url = "jdbc:mysql://localhost:3306/mysql";
        this.username = "root";
        this.password = "";
    }

    public DatabaseHandler(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * *************************Private
     * Methods**************************************
     */
    /*=========Connection Private Methods==========*/
    private void establishConnection(String url, String username, String password) {
        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            System.out.println("Connection is not established");
            ex.printStackTrace();
        }
    }

    private void closeConnection(PreparedStatement pst) {
        try {
            pst.close();
            con.close();
        } catch (SQLException ex) {
            System.out.println("Connection is not Closed");
            ex.printStackTrace();
        }
    }

    /**
     * *************************Public
     * Methods**************************************
     */
    /**
     * checking if user already signUp before
     */
    public int checkUserExistance(String email, String userPassword) {
        PreparedStatement checkUserExistancePst;
        try {

            establishConnection(url, username, password);
            checkUserExistancePst = con.prepareStatement("select email,password from mydb.user where email=?");
            checkUserExistancePst.setString(1, email);
            //checkUserExistancePst.setString(2, password);
            ResultSet result = checkUserExistancePst.executeQuery();
            if (!result.next()) {
                System.out.println("this user doesn't exist");
                return 1;//invalidEmail
            } else {
                String newuserPassword = result.getString("password");
                if (!(newuserPassword.equals(userPassword))) {
                    return 2;//incorrectPassword
                } else {
                    return 3;//goto home every thing is ok
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;//exception 
        }

    }

    /**
     * checking if friend-that user want to add-had signed Up before and have ID
     */
    public int checkFriendExistance(String friendEmail) {
        PreparedStatement checkUserExistancePst;
        try {
            establishConnection(url, username, password);
            checkUserExistancePst = con.prepareStatement("select email from mydb.user where email=?");
            checkUserExistancePst.setString(1, friendEmail);

            ResultSet result = checkUserExistancePst.executeQuery();
            if (!result.next()) {
                System.out.println("Sorry! this email have no S7S ID, please let him join S7S");
                return 0;//friend have no account yet 
            } else {
                return 1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;//exception 
        }

    }

    /**
     * to check if user already added this friend before
     */
    public int checkIfUserAlreadyFreind(String userEmail, String friendEmail) {
        PreparedStatement Pst;
        try {
            establishConnection(url, username, password);
            Pst = con.prepareStatement("select * from mydb.contacts where (userEmail = ? AND friendEmail = ?) OR (userEmail = ? AND friendEmail = ?)");
            Pst.setString(1, userEmail);
            Pst.setString(2, friendEmail);
            Pst.setString(3, friendEmail);
            Pst.setString(4, userEmail);

            ResultSet result = Pst.executeQuery();
            if (!result.next()) {
                return 1;//you can add user as friend 
            } else {
                return 0;//you are already friends 
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;//exception 
        }

    }

    public void createTables() {
        PreparedStatement pst = null;
        try {
            //open connections
            establishConnection(url, username, password);
            //create Tables

            String createUserTable = "CREATE TABLE IF NOT EXISTS `mydb`.`USER` (\n"
                    + "  `EMAIL` VARCHAR(60) NOT NULL,\n"
                    + "  `userName` VARCHAR(45) NOT NULL,\n"
                    + "  `password` VARCHAR(16) NOT NULL,\n"
                    + "  `gender` VARCHAR(5) NULL,\n"
                    + "  `country` VARCHAR(8) NULL,\n"
                    + "  `status` VARCHAR(20) NULL,\n"
                    + "  PRIMARY KEY (`EMAIL`))";
            //prepare the query
            pst = con.prepareStatement(createUserTable);
            pst.executeUpdate();
            String createContactListTable = "CREATE TABLE IF NOT EXISTS `mydb`.`CONTACTS` ("
                    + "  `USER_EMAIL` VARCHAR(60) NOT NULL,"
                    + "  `friendEmail` VARCHAR(60) NOT NULL,"
                    + "  `userCategory` VARCHAR(45) NULL,"
                    + "  `friendCategory` VARCHAR(45) NULL,"
                    + "  `blocked` VARCHAR(25) NULL,"
                    + "  PRIMARY KEY (`USER_EMAIL`, `friendEmail`),"
                    + "  INDEX `fk_USER_has_USER_USER1_idx` (`friendEmail` ASC),"
                    + "  INDEX `fk_USER_has_USER_USER_idx` (`USER_EMAIL` ASC),"
                    + "  CONSTRAINT `fk_USER_has_USER_USER` "
                    + "    FOREIGN KEY (`USER_EMAIL`) "
                    + "    REFERENCES `mydb`.`USER` (`EMAIL`) "
                    + "    ON DELETE NO ACTION "
                    + "    ON UPDATE NO ACTION,"
                    + "  CONSTRAINT `fk_USER_has_USER_USER1` "
                    + "    FOREIGN KEY (`friendEmail`) "
                    + "    REFERENCES `mydb`.`USER` (`EMAIL`) "
                    + "    ON DELETE NO ACTION"
                    + "    ON UPDATE NO ACTION)";
            //prepare the query
            pst = con.prepareStatement(createContactListTable);
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error in query");
        } finally {
            //close connection
            closeConnection(pst);
        }
    }

    /**
     * adding new user to database as signUp process
     */
    public int insertNewUser(User newUser) {
        //checking if user signUp befor or not 
        int result = checkUserExistance(newUser.getUserEmail(), newUser.getUserPassword());
        if (result == 1) //user does not have account
        {

            PreparedStatement pst = null;
            try {
                establishConnection(url, username, password);
                //prepare the query
                pst = con.prepareStatement("INSERT INTO mydb.user(email, userName, password, gender, country,status) VALUES (?, ?, ?, ?,?,?)");
                pst.setString(1, newUser.getUserEmail());
                pst.setString(2, newUser.getUserNickName());
                pst.setString(3, newUser.getUserPassword());
                pst.setString(4, newUser.getUserGender());
                pst.setString(5, newUser.getUserCountry());
                pst.setString(6, newUser.getUserStatus());

                //execute the query 
                int queryResult = pst.executeUpdate();
                System.out.println(queryResult);

            } catch (SQLException ex) {
                System.out.println("insert New User problem -> Can not add new user");
                ex.printStackTrace();
            } finally {
                closeConnection(pst);
            }
            return 1;//user added
        } else {

            return 0;//user already have account 
        }
    }

    /**
     * Adding new Friend to user contact list
     *
     * @param contactList
     */
    public void insertFriend(ContactList contactList) {

        PreparedStatement pst = null;
        try {
            establishConnection(url, username, password);
            //prepare the query
            pst = con.prepareStatement("INSERT INTO mydb.contacts(userEmail, friendEmail,userCategory,friendCategory) VALUES (?,?,?,?)");
            pst.setString(1, contactList.getUser().getUserEmail());
            pst.setString(2, contactList.getFriend().getUserEmail());
            pst.setString(3, contactList.getUserCategory());
            pst.setString(4, contactList.getFriendCategory());
            //execute the query 
            int queryResult = pst.executeUpdate();
            System.out.println("number of rows inserted =" + queryResult);

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error in insert friend query");
        } finally {
            //close connection
            closeConnection(pst);
        }

    }

    //unTested
    public void setContactListCategory(ContactList contactList) {
        PreparedStatement pst = null;
        try {
            establishConnection(url, username, password);
            //prepare the query
            pst = con.prepareStatement("UPDATE contactlist SET ucategory=?, fcategory=? WHERE userEmail=?, friendEmail=?");
            pst.setString(1, contactList.getUserCategory());
            pst.setString(2, contactList.getFriendCategory());
            pst.setString(3, contactList.getUser().getUserEmail());
            pst.setString(4, contactList.getFriend().getUserEmail());
            //execute the query 
            int queryResult = pst.executeUpdate();
            System.out.println("number of rows affected by updating category" + queryResult);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //close connection
            closeConnection(pst);
        }
    }

    public void blockUser(ContactList contactList) {
        PreparedStatement pst = null;
        try {
            establishConnection(url, username, password);
            //prepare the query
            pst = con.prepareStatement("UPDATE mydb.contacts SET blocked=? WHERE userEmail=?, friendemail=?");
            pst.setString(1, contactList.getIsBlocked());
            pst.setString(2, contactList.getUser().getUserEmail());
            pst.setString(3, contactList.getFriend().getUserEmail());

            //execute the query 
            int queryResult = pst.executeUpdate();
            System.out.println(queryResult);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //close connection
            closeConnection(pst);
        }
    }

    public String getUserStatus(String userEmail) {
        PreparedStatement pst = null;
        try {
            establishConnection(url, username, password);
            pst = con.prepareStatement("select status from mydb.user where email = ?");
            pst.setString(1, userEmail);
            ResultSet queryResult = pst.executeQuery();
            queryResult.next();
            String userStatus = queryResult.getString(1);
            return userStatus;

        } catch (SQLException ex) {
            System.out.println("getting user status faild");
            ex.printStackTrace();
            return null;
        } finally {
            //close connection
            closeConnection(pst);
        }

    }

    /**
     * updating user status
     *
     * @param user
     */
    public void updateStatus(User user) {
        PreparedStatement pst = null;
        try {
            establishConnection(url, username, password);
            //prepare the query
            pst = con.prepareStatement("UPDATE mydb.user SET status=? WHERE email=?");
            pst.setString(1, user.getUserStatus());
            pst.setString(2, user.getUserEmail());

            //execute the query 
            int queryResult = pst.executeUpdate();
            System.out.println("nuber of rows affected by updating user status" + queryResult);
            System.out.println("status updated");

        } catch (SQLException ex) {
            System.out.println("update user status faild");
            ex.printStackTrace();
        } finally {
            //close connection
            closeConnection(pst);
        }
    }

    /**
     * getting number of registered users on server
     */
    public int countUsers() {
        PreparedStatement pst = null;
        try {
            establishConnection(url, username, password);
            //prepare the query
            pst = con.prepareStatement("select count(*) from mydb.user");

            //execute the query 
            ResultSet queryResult = pst.executeQuery();
            queryResult.next();
            int numberOfUsers = Integer.parseInt(queryResult.getString(1));
            return numberOfUsers;

        } catch (SQLException ex) {
            System.out.println("getting number of registered users faild");
            ex.printStackTrace();
            return 0;
        } finally {
            //close connection
            closeConnection(pst);
        }
    }

}
