package com.bianyi.item.controller;

import com.bianyi.item.pojo.Brand;
import com.bianyi.item.service.BrandService;
import com.bianyi.pojo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    BrandService brandService;

    /**
     *根据查询条件分页并排序查询品牌信息
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @RequestMapping("/page")
    public ResponseEntity<PageResult<Brand>> pageResultResponseEntity(
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "5")Integer rows,
            @RequestParam(value = "sortBy", required = false)String sortBy,
            @RequestParam(value = "desc", required = false)Boolean desc
    ) {
        PageResult<Brand> pageResult=brandService.findBrandByPage(key,page,rows,sortBy,desc);
        if (CollectionUtils.isEmpty(pageResult.getItems())){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pageResult);
    }

    @PostMapping
    public ResponseEntity addBrand(Brand brand,@RequestParam("cids")List<Long> cids){
        brandService.addBrand(brand,cids);
        //201(已创建)请求成功并且服务器创建了新的资源。
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 添加商品是回显品牌
     * @param cid
     * @return
     */
    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> findBrandByCid(@PathVariable("cid") Long cid){
        List<Brand> list = brandService.findBrandByCid(cid);
        return ResponseEntity.ok(list);
    }
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id){
        Brand brand = brandService.queryBrandById(id);
        return ResponseEntity.ok(brand);
    }
}
