package com.seckill.model.mapper;

import com.seckill.model.entity.ItemKill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemKillMapper {
    /**
     * 查询秒杀商品列表
     * @return
     */
    List<ItemKill> selectAll();

    ItemKill selectById(@Param("id") Integer id);

    int updateKillItem(@Param("killId") Integer killId);



    ItemKill selectByIdV2(@Param("id") Integer id);

    int updateKillItemV2(@Param("killId") Integer killId);

}