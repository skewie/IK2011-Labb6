/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.ArticleDAO;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import model.Article;
import model.Order;
import model.OrderRow;

/**
 *
 * @author Jeff
 */
@ManagedBean(name = "cartBean")
@SessionScoped
public class CartBean implements Serializable{
    
    private Order order;
    private ArticleDAO dao;
    
    public CartBean() {
        order = new Order();
        try {
            dao = ArticleDAO.getInstance();
        } catch (Exception e) {
            System.out.println(e.getClass()+" - "+e.getMessage());
            System.out.println("StackTrace:");
            for (StackTraceElement ste : e.getStackTrace()) {
                System.out.println(ste.toString());
            }
        }
    }

    /*public CartBean(ArrayList<OrderRow> order.getOrderRows()) {
        this.order.getOrderRows() = order.getOrderRows();
    }*/

    public ArrayList<OrderRow> getCartRows() {
        return order.getOrderRows();
    }

    public void addArticle(Article article) {
        if (inCart(article.getArticleId())) {
            increaseItemAmount(article.getArticleId());
        } else {
            order.add(new OrderRow(article));
        }
    }
    
    public void removeArticle(Article article) {
        int index = 0;
        if ((index = getArticleIndex(article)) != -1) {
            order.remove(index);
        }else{
            System.out.println("pointing at index: " + index);
        }
    }
    
    public int getTotalCartRows() {
        return order.getOrderRows().size();
    }
    
    public boolean inCart(int articleId) {
        for (OrderRow row : order.getOrderRows()) {
            if (row.getArticle().getArticleId() == articleId) {
                return true;
            }
        }
        
        return false;
    }
    
    private int getArticleIndex(Article article) {
        for (int i = 0; i < order.getOrderRows().size(); i++) 
            if (order.getOrderRows().get(i).getArticle().getArticleId() == article.getArticleId())
                return i;
        
        return -1;
    }
    
    public void increaseItemAmount(int articleId) {
        for (OrderRow row : order.getOrderRows()) {
            if (row.getArticle().getArticleId() == articleId) {
                row.increaseAmount();
                break;
            }
        }
    }
    
    public void reduceItemAmount(int articleId) {
        for (OrderRow row : order.getOrderRows()) {
            if (row.getArticle().getArticleId() == articleId) {
                row.reduceAmount();
                if(row.getAmount() <= 0)
                    order.getOrderRows().remove(row);
                break;
            }
        }
    }
    
    public void setRowAmount(Article article, int amount) {
        if (amount <= 0) { // Reduceras det till noll eller mindre tar vi bort varan helt.
            removeArticle(article);
        } else {
            for (OrderRow row : order.getOrderRows()) {
                if (row.getArticle().getArticleId() == article.getArticleId()) {
                    row.setAmount(amount);
                    break;
                }
            }
        }
    }
    
    public int getCartTotalItems() {
        int tot = 0;
        
        for (OrderRow row : order.getOrderRows()) {
            tot += row.getAmount();
        }
        
        return tot;
    }
    
    public String getCartTotalPrice() {
        double tot = 0.0;
        
        for (OrderRow row : order.getOrderRows()) {
            tot += row.getAmount() * row.getArticle().getPrice();
        }
        
        return NumberFormat.getCurrencyInstance().format(tot);
    }
    
    public boolean isCartEmpty(){
        return order.getOrderRows().isEmpty();
    }
    
    public String checkOut(Order order) {
        
        this.order.setFirstName(order.getFirstName());
        this.order.setLastName(order.getLastName());
        
        try{
            dao.addToOrder(this.order);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return "";
    }
    
}
