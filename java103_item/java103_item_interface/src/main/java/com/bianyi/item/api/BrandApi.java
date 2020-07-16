package com.bianyi.item.api;

import com.bianyi.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


public interface BrandApi {
    @GetMapping("/brand/{id}")
    public Brand queryBrandById(@PathVariable("id") Long id);
}
