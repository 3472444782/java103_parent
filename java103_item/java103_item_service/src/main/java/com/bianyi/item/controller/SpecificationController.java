package com.bianyi.item.controller;

import com.bianyi.item.pojo.SpecGroup;
import com.bianyi.item.pojo.SpecParam;
import com.bianyi.item.pojo.SpuDetail;
import com.bianyi.item.service.SpecificationService;
import com.bianyi.pojo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/spec")
public class SpecificationController {
    @Autowired
    SpecificationService specificationService;


    @RequestMapping("/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> list = specificationService.queryGroupsByCid(cid);
        return ResponseEntity.ok(list);
    }

    /**
     * 展示添加页面中的副页面数据
     * @param gid
     * @param cid
     * @param generic
     * @param searching
     * @return
     */
    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> queryParams(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam(value = "generic",required = false) Boolean generic,
            @RequestParam(value = "searching",required = false) Boolean searching
    )
    {
        List<SpecParam> list = specificationService.queryParams(gid,cid,generic,searching);
        return ResponseEntity.ok(list);
    }




}
