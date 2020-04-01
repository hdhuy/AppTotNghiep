/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.User;
import View.Login;
import View.Working;

/**
 *
 * @author Admin
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Login log= new Login();
//        log.setVisible(true);
        User u = new User();
        //u.setRole("ROLE_APP_MANAGER");
        u.setRole("ROLE_ADMIN");
        //u.setRole("ROLE_APP_STAFF");
        Working w= new Working(u);
        w.setVisible(true);
    }
    
}
