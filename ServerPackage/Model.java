/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerPackage;

import DataTransferObject.DatabaseHandler;

/**
 *
 * @author Samir
 */
public class Model implements ServerServices{

    DatabaseHandler databaseRefrence = new DatabaseHandler();
    /**
     *
     * @param newUser
     */
    @Override
    public void signUp(User newUser) {
       int result = databaseRefrence.insertNewUser(newUser);
       if(result == 1)
            System.out.println("user inserted");
       else
            System.out.println("user already exist");
    }

    @Override
    public void signIn(String userEmail, String userPassword) {
      int result =  databaseRefrence.checkUserExistance(userEmail,userPassword);
      switch(result)
      {
          case 0:
              System.out.println("exception");
              break;
          case 1:
              System.out.println("invalid email");
              break;
          case 2:
              System.out.println("invliad password");
              break;
          case 3:
              System.out.println("Welcome "+userEmail);
              break;
      }
        
    }

    @Override
    public void addFriend(ContactList contactList) {
       int result =  databaseRefrence.checkFriendExistance(contactList.getFriend().getUserEmail());//check if friend have ID
       if(result == 1)
       {
            System.out.println("friend have ID");
            int isFreind = databaseRefrence.checkIfUserAlreadyFreind(contactList.getUser().getUserEmail(), contactList.getFriend().getUserEmail());
            if(isFreind == 1)
            {
                databaseRefrence.insertFriend(contactList);
            }
            else
                System.out.println("this user already friend with you");
       }
       else
            System.out.println("friend does not have ID");
    }

    @Override
    public void changeStatus(User user) {
        databaseRefrence.updateStatus(user);
    }
    @Override
    public String getUserStatus(String userEmail)
    {
        String userStatus = databaseRefrence.getUserStatus(userEmail);
        return userStatus;
    }
    public int getUsersNumber()
    {
        int numberOfUsers = databaseRefrence.countUsers();
        return numberOfUsers;
    }
    public static void main(String[] args) {
        
       
        Model model  = new Model();
        User samir   = new User("samir@samir.com", "samir1234", "samirGhoneim","Male", "cairo", "busy");
        User toqa    = new User("toqa@toqa.com", "toqa1234", "toqaEid","female", "cairo", "busy");
        User esraa   = new User("esraa@esraa.com", "esraa1234", "esraa","female", "cairo", "avilabel");
        User ramadan = new User("ramadan@ramadan.com", "ramadan1234", "mohamedRamadan","Male", "cairo", "away");
        ContactList contactList = new ContactList(samir, toqa, "Freinds", "ITI", null);
        ContactList contactList2 = new ContactList(toqa, samir, "Freinds", "ITI", null);
//        model.signUp(toqa);
//        model.signUp(esraa);
//        model.signUp(ramadan);
//        model.signIn("samir@samir.com", "samir1234");
//        model.addFriend(contactList2);
//        model.changeStatus(samir);
//        System.out.println(model.getUserStatus("samir@samir.com"));
        model.signIn("sama@asd.com", "samir1234");//case of invalid email
        model.signIn("samir@samir.com", "s234");//case of invalid password
        System.out.println("number of registered users = "+model.getUsersNumber());
        
    }
}
