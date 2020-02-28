package com.seckill.model.dto;

import com.seckill.model.entity.ItemKillSuccess;
import lombok.Data;

import java.io.Serializable;

/**
 * 秒杀成功的用户信息
 */
@Data
public class KillSuccessUserInfo extends ItemKillSuccess implements Serializable {

    private String userName;

    private String phone;

    private String email;

    private String itemName;

    @Override
    public String toString() {
        return super.toString() + "\nKillSuccessUserInfo{" +
                "userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", itemName='" + itemName + '\'' +
                '}';
    }
}