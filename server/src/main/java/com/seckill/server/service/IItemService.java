package com.seckill.server.service;

import com.seckill.model.entity.ItemKill;

import java.util.List;

/**
 *
 */
public interface IItemService {

    List<ItemKill> getKillItems() throws Exception;

    ItemKill getKillDetail(Integer id) throws Exception;
}
