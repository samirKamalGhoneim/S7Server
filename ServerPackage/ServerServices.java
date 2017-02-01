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
public interface ServerServices {
    void signUp(User newUser);
    void signIn(String userEmail,String userPassword);
    void addFriend(ContactList contactList);
    void changeStatus(User user);
    String getUserStatus(String userEmail);
}
