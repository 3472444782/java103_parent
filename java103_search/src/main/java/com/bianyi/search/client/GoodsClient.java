package com.bianyi.search.client;

import com.bianyi.item.api.GoodApi;
import com.bianyi.item.pojo.Sku;
import com.bianyi.item.pojo.SpuBo;
import com.bianyi.item.pojo.SpuDetail;
import com.bianyi.pojo.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(value = "java103-item-service")
public interface GoodsClient extends GoodApi {
    /**
     * 获取商品页面
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
    @RequestMapping("/spu/page")
    public PageResult<SpuBo> findQueryAndPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    );

    *//**
     * 展示修改商品窗口spu的内容
     * @param spuId
     * @return
     *//*
    @GetMapping("/spu/detail/{spuId}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuId") Long spuId);

    *//**
     * 展示修改商品窗口sku的内容
     *//*
    @GetMapping("/sku/list")
    public List<Sku> querySkuBySpuId(@RequestParam("id") Long spuId);*/
}
