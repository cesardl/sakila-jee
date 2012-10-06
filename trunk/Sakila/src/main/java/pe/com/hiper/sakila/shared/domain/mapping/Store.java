package pe.com.hiper.sakila.shared.domain.mapping;
// Generated 20-sep-2012 10:21:09 by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Store generated by hbm2java
 */
public class Store  implements java.io.Serializable {


     private Byte storeId;
     private Staff staff;
     private Address address;
     private Date lastUpdate;
     private Set inventories = new HashSet(0);
     private Set customers = new HashSet(0);
     private Set staffs = new HashSet(0);

    public Store() {
    }

	
    public Store(Staff staff, Address address, Date lastUpdate) {
        this.staff = staff;
        this.address = address;
        this.lastUpdate = lastUpdate;
    }
    public Store(Staff staff, Address address, Date lastUpdate, Set inventories, Set customers, Set staffs) {
       this.staff = staff;
       this.address = address;
       this.lastUpdate = lastUpdate;
       this.inventories = inventories;
       this.customers = customers;
       this.staffs = staffs;
    }
   
    public Byte getStoreId() {
        return this.storeId;
    }
    
    public void setStoreId(Byte storeId) {
        this.storeId = storeId;
    }
    public Staff getStaff() {
        return this.staff;
    }
    
    public void setStaff(Staff staff) {
        this.staff = staff;
    }
    public Address getAddress() {
        return this.address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    public Date getLastUpdate() {
        return this.lastUpdate;
    }
    
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public Set getInventories() {
        return this.inventories;
    }
    
    public void setInventories(Set inventories) {
        this.inventories = inventories;
    }
    public Set getCustomers() {
        return this.customers;
    }
    
    public void setCustomers(Set customers) {
        this.customers = customers;
    }
    public Set getStaffs() {
        return this.staffs;
    }
    
    public void setStaffs(Set staffs) {
        this.staffs = staffs;
    }




}


