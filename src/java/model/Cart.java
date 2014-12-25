/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.text.NumberFormat;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Jeff
 */
@ManagedBean(name = "cartBean")
@SessionScoped
public class Cart {
    private ArrayList<CartRow> cartRows;

    public Cart() {
        cartRows = new ArrayList();
    }

    public Cart(ArrayList<CartRow> cartRows) {
        this.cartRows = cartRows;
    }

    public ArrayList<CartRow> getCartRows() {
        return cartRows;
    }

    public void addArticle(Article article) {
        if (inCart(article.getArticleId())) {
            increaseItemAmount(article.getArticleId());
        } else {
            cartRows.add(new CartRow(article));
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
        for (CartRow row : cartRows) {
            if (row.getArticle().getArticleId() == articleId) {
                return true;
            }
        }
        
        return false;
    }
    
    private int getArticleIndex(Article article) {
        for (int i = 0; i < 0; i++) 
            if (cartRows.get(i).getArticle().getArticleId() == article.getArticleId())
                return i;
        
        return -1;
    }
    
    public void increaseItemAmount(int articleId) {
        for (CartRow row : cartRows) {
            if (row.getArticle().getArticleId() == articleId) {
                row.increaseAmount();
                break;
            }
        }
    }
    
    public void reduceItemAmount(int articleId) {
        for (CartRow row : cartRows) {
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
            for (CartRow row : cartRows) {
                if (row.getArticle().getArticleId() == article.getArticleId()) {
                    row.setAmount(amount);
                    break;
                }
            }
        }
    }
    
    public int getCartTotalItems() {
        int tot = 0;
        
        for (CartRow row : cartRows) {
            tot += row.getAmount();
        }
        
        return tot;
    }
    
    public String getCartTotalPrice() {
        double tot = 0.0;
        
        for (CartRow row : cartRows) {
            tot += row.getAmount() * row.getArticle().getPrice();
        }
        
        return NumberFormat.getCurrencyInstance().format(tot);
    }
}
