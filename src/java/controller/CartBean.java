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
import model.OrderRow;

/**
 *
 * @author Jeff
 */
@ManagedBean(name = "cartBean")
@SessionScoped
public class CartBean implements Serializable{
    
    private final ArrayList<OrderRow> cartRows;
    private ArticleDAO dao;
    
    public CartBean() {
        cartRows = new ArrayList();
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

    public CartBean(ArrayList<OrderRow> cartRows) {
        this.cartRows = cartRows;
    }

    public ArrayList<OrderRow> getCartRows() {
        return cartRows;
    }

    public void addArticle(Article article) {
        if (inCart(article.getArticleId())) {
            increaseItemAmount(article.getArticleId());
        } else {
            cartRows.add(new OrderRow(article));
        }
    }
    
    public void removeArticle(Article article) {
        System.out.println("removing: " + article.getName());
        int index = 0;
        System.out.println("cart size: " + cartRows.size());
        if ((index = getArticleIndex(article)) != -1) {
            System.out.println("found it!");
            cartRows.remove(index);
        }else{
            System.out.println("pointing at index: " + index);
        }
    }
    
    public int getTotalCartRows() {
        return cartRows.size();
    }
    
    public boolean inCart(int articleId) {
        for (OrderRow row : cartRows) {
            if (row.getArticle().getArticleId() == articleId) {
                return true;
            }
        }
        
        return false;
    }
    
    private int getArticleIndex(Article article) {
        for (int i = 0; i < cartRows.size(); i++) 
            if (cartRows.get(i).getArticle().getArticleId() == article.getArticleId())
                return i;
        
        return -1;
    }
    
    public void increaseItemAmount(int articleId) {
        for (OrderRow row : cartRows) {
            if (row.getArticle().getArticleId() == articleId) {
                row.increaseAmount();
                break;
            }
        }
    }
    
    public void reduceItemAmount(int articleId) {
        for (OrderRow row : cartRows) {
            if (row.getArticle().getArticleId() == articleId) {
                row.reduceAmount();
                if(row.getAmount() <= 0)
                    cartRows.remove(row);
                break;
            }
        }
    }
    
    public void setRowAmount(Article article, int amount) {
        if (amount <= 0) { // Reduceras det till noll eller mindre tar vi bort varan helt.
            removeArticle(article);
        } else {
            for (OrderRow row : cartRows) {
                if (row.getArticle().getArticleId() == article.getArticleId()) {
                    row.setAmount(amount);
                    break;
                }
            }
        }
    }
    
    public int getCartTotalItems() {
        int tot = 0;
        
        for (OrderRow row : cartRows) {
            tot += row.getAmount();
        }
        
        return tot;
    }
    
    public String getCartTotalPrice() {
        double tot = 0.0;
        
        for (OrderRow row : cartRows) {
            tot += row.getAmount() * row.getArticle().getPrice();
        }
        
        return NumberFormat.getCurrencyInstance().format(tot);
    }
    
    public String checkOut(){
        try{
            dao.addToOrder(cartRows);
            //cartRows.clear();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return "";
    }
    
    public boolean isCartNotEmpty(){
        if(cartRows.isEmpty() == true){
            return false;
        }else{
            return true;
        }
    }
    
}
