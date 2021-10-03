package com.tellimusveod.webapi.entity;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "oder")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @TableGenerator(
            name="id",
            table="GENERATOR_TABLE",
            pkColumnName = "key",
            valueColumnName = "next",
            pkColumnValue="course",
            allocationSize=30
    )
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "offerer_id")
    private Integer offererId;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "is_done")
    private boolean isDone;

    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;


    @Column(name = "orderer_id")
    private Integer ordererId;

    public OrderEntity() {

    }

    public OrderEntity(String title, String description, Double cost, Double longitude, Double latitude) {
        this.setTitle(title);
        this.setDescription(description);
        this.setCost(cost);
        this.setLongitude(longitude);
        this.setLatitude(latitude);
        this.setCreatedAt(new Date());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) createdAt = new Date();
        if (this.updatedAt == null) updatedAt = new Date();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = new Date();
    }

    @PreRemove
    protected void preRemove() {
        this.updatedAt = new Date();
    }

    public Double getLatitude() {
        return latitude;
    }

    private void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    private void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public Double getCost() {
        return cost;
    }

    private void setCost(Double cost) {
        this.cost = cost;
    }


    public Integer getOrdererId() {
        return ordererId;
    }

    public void setOrdererId(Integer ordererId) {
        this.ordererId = ordererId;
    }

    public Integer getOffererId() {
        return offererId;
    }

    public void setOffererId(Integer offererId) {
        this.offererId = offererId;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

