package com.bianyi.item.controller;

import com.bianyi.item.pojo.Category;
import com.bianyi.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @RequestMapping("/list")
    public ResponseEntity<List<Category>> getCategoryById(Long pid) {
        if (pid == null || pid < 0) {
            //相当于400
            return ResponseEntity.badRequest().build();
        }
        List<Category> categories = categoryService.getCategoryById(pid);
        if (CollectionUtils.isEmpty(categories)) {
            //响应404
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categories);
    }

    /**
     * 根据商品分类id，查询商品分类name
     * @param ids
     * @return
     */
    @RequestMapping("/names")
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids") List<Long> ids){
        List<String> list = categoryService.queryNamesByIds(ids);
        return ResponseEntity.ok(list);
    }

}
