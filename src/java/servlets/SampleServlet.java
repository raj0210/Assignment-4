/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets;

import cnnct.Cnnct;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author c0646395
 */

@Path("/products")
public class SampleServlet{
    

@GET
@Produces("application/json; charset=UTF-8")
public String get() throws SQLException 
{
    JSONArray jArray = new JSONArray();
    Connection con = Cnnct.getConnection();
    String qry = "select * FROM product";
    PreparedStatement prprdstmnt = con.prepareStatement(qry);
    ResultSet rs = prprdstmnt.executeQuery();
    while (rs.next()) 
    {
        int col = rs.getMetaData().getColumnCount();
        JSONObject jData = new JSONObject();
        for (int i = 0; i < col; i++) 
        {
            String strng = rs.getMetaData().getColumnLabel(i + 1).toLowerCase();
            Object obj = rs.getObject(i + 1);
            jData.put(strng, obj);
        }
        jArray.add(jData);

    }

    return jArray.toJSONString();
    }
private String oneRes(String query, String... params) 
{
    StringBuilder strngbldr = new StringBuilder();
    try (Connection con = Cnnct.getConnection()) 
    {
        PreparedStatement prprdstmnt = con.prepareStatement(query);
        for (int i = 1; i <= params.length; i++) 
        {
            prprdstmnt.setString(i, params[i - 1]);
        }
        ResultSet rs = prprdstmnt.executeQuery();
        while (rs.next()) 
        {
            strngbldr.append(String.format("{ \"ProductID\" : %d, \"Name\": \"%s\", \"Description\": \"%s\", \"Quantity\": %s }", rs.getInt("productID"), rs.getString("Name"), rs.getString("Description"), rs.getInt("Quantity")));
        }
    } 
    catch (SQLException ex) 
    {
        Logger.getLogger(SampleServlet.class.getName()).log(Level.SEVERE, null, ex);
    }
    return strngbldr.toString();
    }
 private String results(String query, String... params) 
 {
    StringBuilder strngbldr = new StringBuilder();
    try (Connection con = Cnnct.getConnection()) 
    {
        PreparedStatement prprdstmnt = con.prepareStatement(query);
        for (int i = 1; i <= params.length; i++) 
        {
            prprdstmnt.setString(i, params[i - 1]);
        }
        ResultSet rs = prprdstmnt.executeQuery();
        strngbldr.append("[");
        while (rs.next()) 
        {
            strngbldr.append(String.format("{ \"ProductID\" : %d, \"Name\": \"%s\", \"Description\": \"%s\", \"Quantity\": %d },\n", rs.getInt("ProductID"), rs.getString("Name"), rs.getString("Description"), rs.getInt("Quantity")));
        }
        strngbldr.setLength(strngbldr.length() - 2);
        strngbldr.append("]");
        } 
    catch (SQLException ex) 
    {
        Logger.getLogger(SampleServlet.class.getName()).log(Level.SEVERE, null, ex);
    }
    return strngbldr.toString();
}

 @POST
@Path("{ProductID}")
public void post(String str) throws SQLException, ParseException, org.json.simple.parser.ParseException 
{
  JSONObject jsonData = (JSONObject) new JSONParser().parse(str);
  String Name = (String) jsonData.get("Name");
  String Description = (String) jsonData.get("Description");
  long Quantity = (long) jsonData.get("Quantity");
  insert("insert into product (Name, Description, Quantity) VALUES (?, ?, ?)", Name, Description, Quantity);
}
    


private int insert(String query, String Name, String Description, long Quantity) 
{
    int num = 0;
    ArrayList params = new ArrayList();
    params.add(Name);
    params.add(Description);
    params.add(Quantity);
    try (Connection con = Cnnct.getConnection()) 
    {
        PreparedStatement prprdstmnt = con.prepareStatement(query);
        for (int i = 1; i <= params.size(); i++) 
        {
            prprdstmnt.setString(i, params.get(i - 1).toString());
        }
        num = prprdstmnt.executeUpdate();
    } 
    catch (SQLException ex) 
    {
        Logger.getLogger(SampleServlet.class.getName()).log(Level.SEVERE, null, ex);
    }
    return num;
    }
@DELETE
@Path("{ProductID}")
public void dlt(@PathParam("ProductID") int id) throws IOException, SQLException {
    Connection con = Cnnct.getConnection();
    String qry = "delete from product where ProductID =" + id;
    PreparedStatement prprdstmnt = con.prepareStatement(qry);
    prprdstmnt.execute();
}

private int delete(String query, int id) 
{
    int num = 0;
    try (Connection con = Cnnct.getConnection()) 
    {
        PreparedStatement prprdstmnt = con.prepareStatement(query);
        prprdstmnt.setLong(1, id);
        num = prprdstmnt.executeUpdate();
    } 
    catch (SQLException ex) 
    {
        System.out.println(ex);
        Logger.getLogger(SampleServlet.class.getName()).log(Level.SEVERE, null, ex);
    }
    return num;
}

 
}
