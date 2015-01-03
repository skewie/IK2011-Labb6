/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import DAO.ArticleDAO;
import controller.CartBean;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Jeff
 */
@ManagedBean(name = "order")
@SessionScoped
public class Order implements Serializable{
    
    private String firstName;
    private String lastName;
    private ArrayList<OrderRow> rows;
    
    public Order(String firsName, String lastName, ArrayList<OrderRow> row){
        this.firstName = firsName;
        this.lastName = lastName;
        this.rows = row;
    }
    
    public Order(){
        rows = new ArrayList();
    }
    
    public void add(OrderRow row) {
        rows.add(row);
    }
    
    public void remove(int index) {
        rows.remove(index);
    }
    
    public void setFirstName(String fname){
        this.firstName = fname;
    }
    
    public String getFirstName(){
        return this.firstName;
    }
    
    public void setLastName(String lname){
        this.lastName = lname;
    }
    
    public String getLastName(){
        return this.lastName;
    }
    
    public void setOrderRow(ArrayList<OrderRow> rows){
        this.rows = rows;
    }
    
    public ArrayList<OrderRow> getOrderRows(){
        return this.rows;
    }
}
