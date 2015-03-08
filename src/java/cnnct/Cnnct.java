/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cnnct;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author c0646395
 */
public class Cnnct {
       
    
public static Connection getConnection() throws SQLException 
{
    Connection con = null;
    try
    {
        Class.forName("com.mysql.jdbc.Driver");
        String jdbc = "jdbc:mysql://localhost/product";
        String user = "root";
        String password = "";
        con = DriverManager.getConnection(jdbc, user, password);
    }
    catch (ClassNotFoundException | SQLException ex) 
    {
        Logger.getLogger(Cnnct.class.getName()).log(Level.SEVERE, null, ex);
    }
    return con;
}
    
}
