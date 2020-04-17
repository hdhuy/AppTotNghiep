package Model;
// Generated Mar 26, 2020 10:12:20 PM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * OrderApp generated by hbm2java
 */
public class OrderApp  implements java.io.Serializable {


     private Long id;
     private User user;
     private Date createdAt;
     private Date updatedAt;
     private long totalAmount;
     private Set<OrderAppDetail> orderAppDetails = new HashSet<OrderAppDetail>(0);

    public OrderApp() {
    }

	
    public OrderApp(User user, long totalAmount) {
        this.user = user;
        this.totalAmount = totalAmount;
    }
    public OrderApp(User user, Date createdAt, Date updatedAt, long totalAmount, Set<OrderAppDetail> orderAppDetails) {
       this.user = user;
       this.createdAt = createdAt;
       this.updatedAt = updatedAt;
       this.totalAmount = totalAmount;
       this.orderAppDetails = orderAppDetails;
    }
   
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
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
    public long getTotalAmount() {
        return this.totalAmount;
    }
    
    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }
    public Set<OrderAppDetail> getOrderAppDetails() {
        return this.orderAppDetails;
    }
    
    public void setOrderAppDetails(Set<OrderAppDetail> orderAppDetails) {
        this.orderAppDetails = orderAppDetails;
    }




}


