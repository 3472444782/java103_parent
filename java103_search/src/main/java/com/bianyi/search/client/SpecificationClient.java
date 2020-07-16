package com.bianyi.search.client;

import com.bianyi.item.api.SpecificationApi;
import com.bianyi.item.pojo.SpecParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(value = "java103-item-service")
public interface SpecificationClient extends SpecificationApi {
   /* *//**
     * 展示添加页面中的副页面数据
     * @param gid
     * @param cid
     * @param generic
     * @param searching
     * @return
     *//*
    @GetMapping("/spec/params")
    public List<SpecParam> queryParams(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam(value = "generic",required = false) Boolean generic,
            @RequestParam(value = "searching",required = false) Boolean searching
    );*/
}
