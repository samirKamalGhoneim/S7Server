/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerPackage;

/**
 *
 * @author Samir
 */
public class User {
    
   private String userEmail;
   private String userPassword;
   private String userNickName;
   private String userGender;
   private String userCountry;
   private String userStatus;

   ///// Constructors
   
   public User ()
   {    
       userEmail = "example@example.com";
       userNickName = "ChatUserITI";
       userPassword = "1234";
       userGender = "Male";
       userCountry = "USA";
       userStatus = "Online";
   }
    
   public User ( String userEmail, String userPassword, String userNickName, String userGender, String userCountry, String userStatus )
   {
       this.userEmail = userEmail;
       this.userPassword = userPassword;
       this.userNickName = userNickName;
       this.userGender = userGender;
       this.userCountry = userCountry;
       this.userStatus = userStatus;
   }
   
   /////// Setters and Getters

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
   
    
}
