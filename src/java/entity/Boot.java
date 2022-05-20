/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author Melnikov
 */
@Entity
public class Boot implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String caption;
    @OneToOne
    private List<Manufactor> authors;
    private int releaseyear;
    private String price;
    private String cover;
    private int quantity ;
    
    public Boot() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<Manufactor> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Manufactor> authors) {
        this.authors = authors;
    }

    public int getReleaseyear() {
        return releaseyear;
    }

    public void setReleaseyear(int releaseyear) {
        this.releaseyear = releaseyear;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getDecimalPrice() {
        return new BigDecimal(price);
    }

    public void setDecimalPrice(BigDecimal price) {
        this.price = price.toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.caption);
        hash = 79 * hash + Objects.hashCode(this.authors);
        hash = 79 * hash + this.releaseyear;
        hash = 79 * hash + Objects.hashCode(this.price);
        hash = 79 * hash + Objects.hashCode(this.cover);
        hash = 79 * hash + this.quantity;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Boot other = (Boot) obj;
        if (this.releaseyear != other.releaseyear) {
            return false;
        }
        if (this.quantity != other.quantity) {
            return false;
        }
        if (!Objects.equals(this.caption, other.caption)) {
            return false;
        }
        if (!Objects.equals(this.price, other.price)) {
            return false;
        }
        if (!Objects.equals(this.cover, other.cover)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.authors, other.authors)) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        StringBuilder authorsStr = new StringBuilder();
        authorsStr.append("[");
        for(Manufactor a : authors){
            authorsStr.append(a.getName());
            authorsStr.append(" ");
            authorsStr.append(a.getCountry());
            authorsStr.append(". ");
        }
        authorsStr.append("]");
        return "Book{" 
                + "id=" + id 
                + ", caption=" + caption 
                + ", manufactor=" + authorsStr.toString()
                + ", Year=" + releaseyear 
                + ", price=" + price 
                + '}';
    }

    

   
}
