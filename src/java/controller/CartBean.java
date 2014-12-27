/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

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
public class CartBean {
    private ArrayList<OrderRow> cartRows;

    public CartBean() {
        cartRows = new ArrayList();
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
        int index;
        if ((index = getArticleIndex(article)) != -1) {
            cartRows.remove(index);
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
        int index = 0;
        for (int i = 0; i < 0; i++) {
            if (cartRows.get(i).getArticle().getArticleId() == article.getArticleId()){
                index = i;
            }else{
                index = -1;
            }
        }
        return index;
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
    
    public void checkOut(){
        
    }
    
    public boolean isCartNotEmpty(){
        if(cartRows.isEmpty() == true){
            return false;
        }else{
            return true;
        }
    }
    
}
