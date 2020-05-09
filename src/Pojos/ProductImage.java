package Pojos;
// Generated Apr 23, 2020 4:34:16 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * ProductImage generated by hbm2java
 */
public class ProductImage  implements java.io.Serializable {


     private Long id;
     private Product product;
     private Date createdAt;
     private Date updatedAt;
     private byte[] data;
     private Boolean isPrimary;
     private String path;
     private Integer size;

    public ProductImage() {
    }

	
    public ProductImage(Product product) {
        this.product = product;
    }
    public ProductImage(Product product, Date createdAt, Date updatedAt, byte[] data, Boolean isPrimary, String path, Integer size) {
       this.product = product;
       this.createdAt = createdAt;
       this.updatedAt = updatedAt;
       this.data = data;
       this.isPrimary = isPrimary;
       this.path = path;
       this.size = size;
    }
   
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    public byte[] getData() {
        return this.data;
    }
    
    public void setData(byte[] data) {
        this.data = data;
    }
    public Boolean getIsPrimary() {
        return this.isPrimary;
    }
    
    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
    public String getPath() {
        return this.path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    public Integer getSize() {
        return this.size;
    }
    
    public void setSize(Integer size) {
        this.size = size;
    }




}


