package Pojos;
// Generated Apr 23, 2020 4:34:16 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * ProductColor generated by hbm2java
 */
public class ProductColor  implements java.io.Serializable {


     private Long id;
     private Color color;
     private Product product;
     private Date createdAt;
     private Date updatedAt;

    public ProductColor() {
    }

	
    public ProductColor(Color color, Product product) {
        this.color = color;
        this.product = product;
    }
    public ProductColor(Color color, Product product, Date createdAt, Date updatedAt) {
       this.color = color;
       this.product = product;
       this.createdAt = createdAt;
       this.updatedAt = updatedAt;
    }
   
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    public Color getColor() {
        return this.color;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    public Product getProduct() {
        return this.product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    public Date getCreatedAt() {
        return this.createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public Date getUpdatedAt() {
        return this.updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }




}


