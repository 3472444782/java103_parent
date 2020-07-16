package com.bianyi.search.client;

import com.bianyi.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(value = "java103-item-service")
public interface CategoryClient extends CategoryApi {
   /* @RequestMapping("/category/names")
    public List<String> queryNamesByIds(@RequestParam("ids") List<Long> ids);*/
}
