package Model;
// Generated Mar 26, 2020 10:12:20 PM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * OrderWebsite generated by hbm2java
 */
public class OrderWebsite  implements java.io.Serializable {


     private Long id;
     private User user;
     private Date createdAt;
     private Date updatedAt;
     private String deliveryStatus;
     private String paymentStatus;
     private long totalAmount;
     private Set<OrderWebsiteDetail> orderWebsiteDetails = new HashSet<OrderWebsiteDetail>(0);

    public OrderWebsite() {
    }

	
    public OrderWebsite(User user, String deliveryStatus, String paymentStatus, long totalAmount) {
        this.user = user;
        this.deliveryStatus = deliveryStatus;
        this.paymentStatus = paymentStatus;
        this.totalAmount = totalAmount;
    }
    public OrderWebsite(User user, Date createdAt, Date updatedAt, String deliveryStatus, String paymentStatus, long totalAmount, Set<OrderWebsiteDetail> orderWebsiteDetails) {
       this.user = user;
       this.createdAt = createdAt;
       this.updatedAt = updatedAt;
       this.deliveryStatus = deliveryStatus;
       this.paymentStatus = paymentStatus;
       this.totalAmount = totalAmount;
       this.orderWebsiteDetails = orderWebsiteDetails;
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
    public String getDeliveryStatus() {
        return this.deliveryStatus;
    }
    
    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
    public String getPaymentStatus() {
        return this.paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    public long getTotalAmount() {
        return this.totalAmount;
    }
    
    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }
    public Set<OrderWebsiteDetail> getOrderWebsiteDetails() {
        return this.orderWebsiteDetails;
    }
    
    public void setOrderWebsiteDetails(Set<OrderWebsiteDetail> orderWebsiteDetails) {
        this.orderWebsiteDetails = orderWebsiteDetails;
    }




}


