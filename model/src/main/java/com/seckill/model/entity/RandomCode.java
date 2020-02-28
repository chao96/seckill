package com.seckill.model.entity;

public class RandomCode {
    private Integer id;

    private String code;

    public RandomCode(Integer id, String code) {
        this.id = id;
        this.code = code;
    }

    public RandomCode() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }
}