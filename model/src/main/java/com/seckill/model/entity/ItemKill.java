package com.seckill.model.entity;

import java.util.Date;

public class ItemKill {
    private Integer id;

    private Integer itemId;

    private Integer total;

    private Date startTime;

    private Date endTime;

    private Byte isActive;

    private Date createTime;

    private String itemName;

    private Integer canKill;

    public ItemKill(Integer id, Integer itemId, Integer total, Date startTime, Date endTime, Byte isActive, Date createTime) {
        this.id = id;
        this.itemId = itemId;
        this.total = total;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = isActive;
        this.createTime = createTime;
    }

    public ItemKill() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Byte getIsActive() {
        return isActive;
    }

    public void setIsActive(Byte isActive) {
        this.isActive = isActive;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getCanKill() {
        return canKill;
    }

    public void setCanKill(Integer canKill) {
        this.canKill = canKill;
    }
}