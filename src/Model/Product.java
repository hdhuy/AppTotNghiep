package Model;
// Generated Mar 26, 2020 10:12:20 PM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Product generated by hbm2java
 */
public class Product  implements java.io.Serializable {


     private Long id;
     private Brand brand;
     private Category category;
     private Date createdAt;
     private Date updatedAt;
     private String color;
     private String description;
     private String image;
     private boolean isDelete;
     private String name;
     private long price;
     private Set<Comment> comments = new HashSet<Comment>(0);
     private Set<ProductSize> productSizes = new HashSet<ProductSize>(0);

    public Product() {
    }

	
    public Product(Brand brand, Category category, boolean isDelete, String name, long price) {
        this.brand = brand;
        this.category = category;
        this.isDelete = isDelete;
        this.name = name;
        this.price = price;
    }
    public Product(Brand brand, Category category, Date createdAt, Date updatedAt, String color, String description, String image, boolean isDelete, String name, long price, Set<Comment> comments, Set<ProductSize> productSizes) {
       this.brand = brand;
       this.category = category;
       this.createdAt = createdAt;
       this.updatedAt = updatedAt;
       this.color = color;
       this.description = description;
       this.image = image;
       this.isDelete = isDelete;
       this.name = name;
       this.price = price;
       this.comments = comments;
       this.productSizes = productSizes;
    }
   
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    public Brand getBrand() {
        return this.brand;
    }
    
    public void setBrand(Brand brand) {
        this.brand = brand;
    }
    public Category getCategory() {
        return this.category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
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
    public String getColor() {
        return this.color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImage() {
        return this.image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    public boolean isIsDelete() {
        return this.isDelete;
    }
    
    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public long getPrice() {
        return this.price;
    }
    
    public void setPrice(long price) {
        this.price = price;
    }
    public Set<Comment> getComments() {
        return this.comments;
    }
    
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
    public Set<ProductSize> getProductSizes() {
        return this.productSizes;
    }
    
    public void setProductSizes(Set<ProductSize> productSizes) {
        this.productSizes = productSizes;
    }




}


