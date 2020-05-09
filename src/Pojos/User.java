package Pojos;
// Generated Apr 23, 2020 4:34:16 PM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * User generated by hbm2java
 */
public class User  implements java.io.Serializable {


     private Long id;
     private Date createdAt;
     private Date updatedAt;
     private String address;
     private Date dateOfBirth;
     private String email;
     private Boolean gender;
     private byte[] imageData;
     private String imagePath;
     private boolean isDelete;
     private String name;
     private String password;
     private String phone;
     private String role;
     private Set orderApps = new HashSet(0);
     private Set orderWebs = new HashSet(0);
     private Set productReviews = new HashSet(0);
     private Set productViews = new HashSet(0);
     private Set userWishlists = new HashSet(0);
     private Set cartItems = new HashSet(0);

    public User() {
    }

	
    public User(String email, boolean isDelete, String name, String password) {
        this.email = email;
        this.isDelete = isDelete;
        this.name = name;
        this.password = password;
    }
    public User(Date createdAt, Date updatedAt, String address, Date dateOfBirth, String email, Boolean gender, byte[] imageData, String imagePath, boolean isDelete, String name, String password, String phone, String role, Set orderApps, Set orderWebs, Set productReviews, Set productViews, Set userWishlists, Set cartItems) {
       this.createdAt = createdAt;
       this.updatedAt = updatedAt;
       this.address = address;
       this.dateOfBirth = dateOfBirth;
       this.email = email;
       this.gender = gender;
       this.imageData = imageData;
       this.imagePath = imagePath;
       this.isDelete = isDelete;
       this.name = name;
       this.password = password;
       this.phone = phone;
       this.role = role;
       this.orderApps = orderApps;
       this.orderWebs = orderWebs;
       this.productReviews = productReviews;
       this.productViews = productViews;
       this.userWishlists = userWishlists;
       this.cartItems = cartItems;
    }
   
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }
    
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public Boolean getGender() {
        return this.gender;
    }
    
    public void setGender(Boolean gender) {
        this.gender = gender;
    }
    public byte[] getImageData() {
        return this.imageData;
    }
    
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
    public String getImagePath() {
        return this.imagePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPhone() {
        return this.phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getRole() {
        return this.role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    public Set getOrderApps() {
        return this.orderApps;
    }
    
    public void setOrderApps(Set orderApps) {
        this.orderApps = orderApps;
    }
    public Set getOrderWebs() {
        return this.orderWebs;
    }
    
    public void setOrderWebs(Set orderWebs) {
        this.orderWebs = orderWebs;
    }
    public Set getProductReviews() {
        return this.productReviews;
    }
    
    public void setProductReviews(Set productReviews) {
        this.productReviews = productReviews;
    }
    public Set getProductViews() {
        return this.productViews;
    }
    
    public void setProductViews(Set productViews) {
        this.productViews = productViews;
    }
    public Set getUserWishlists() {
        return this.userWishlists;
    }
    
    public void setUserWishlists(Set userWishlists) {
        this.userWishlists = userWishlists;
    }
    public Set getCartItems() {
        return this.cartItems;
    }
    
    public void setCartItems(Set cartItems) {
        this.cartItems = cartItems;
    }




}


