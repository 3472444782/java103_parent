package com.bianyi.item.controller;

import com.bianyi.item.pojo.Sku;
import com.bianyi.item.pojo.SpuBo;
import com.bianyi.item.pojo.SpuDetail;
import com.bianyi.item.service.GoodService;
import com.bianyi.pojo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GoodsController {
    @Autowired
    GoodService goodService;

    /**
     * 获取商品页面
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/spu/page")
    public ResponseEntity<PageResult<SpuBo>> findQueryAndPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    ) {
        PageResult<SpuBo> pageResult = goodService.findQueryAndPage(key, saleable, page, rows);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 保存商品信息
     *
     * @param spuBo
     * @return
     */
    @PostMapping("/goods")
    public ResponseEntity<Void> saveSpuBo(@RequestBody SpuBo spuBo) {
        goodService.savaSpuBo(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 展示修改商品窗口spu的内容
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId") Long spuId) {
        SpuDetail spuDetail = goodService.querySpuDetailBySpuId(spuId);
        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 展示修改商品窗口sku的内容
     */
    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long spuId) {
        List<Sku> list = goodService.querySkuBySpuId(spuId);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/goods")
    public ResponseEntity<Void> updateSpuBo(@RequestBody SpuBo spuBo) {
        goodService.updateSpuBo(spuBo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
