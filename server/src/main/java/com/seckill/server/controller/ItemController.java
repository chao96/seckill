package com.seckill.server.controller;

import com.seckill.model.entity.ItemKill;
import com.seckill.server.service.IItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 秒杀货品
 *
 * @author xuchao
 * @since 2020/2/26 16:29
 */
@Controller
@RequestMapping("/item")
public class ItemController {

    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private IItemService itemService;

    /**
     * 获取商品列表
     */
    @GetMapping(value = {"/", "/index", "/list", "/index.html"})
    public String list(ModelMap modelMap) {
        try {
            //获取待秒杀商品列表
            List<ItemKill> list = itemService.getKillItems();
            modelMap.put("list", list);

            log.info("获取待秒杀商品列表-数据：{}", list);
        } catch (Exception e) {
            log.error("获取待秒杀商品列表-发生异常：", e.fillInStackTrace());
            return "redirect:/base/error";
        }
        return "list";
    }

    /**
     * 获取待秒杀商品的详情
     *
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable Integer id, ModelMap modelMap) {
        if (id == null || id <= 0) {
            return "redirect:/base/error";
        }
        try {
            ItemKill detail = itemService.getKillDetail(id);
            modelMap.put("detail", detail);
        } catch (Exception e) {
            log.error("获取待秒杀商品的详情-发生异常：id={}", id, e.fillInStackTrace());
            return "redirect:/base/error";
        }
        return "info";
    }
}





























