package com.bianyi.search.client;
import com.bianyi.item.api.BrandApi;
import com.bianyi.item.pojo.Brand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "java103-item-service")
public interface BrandClient extends BrandApi {
   /* @GetMapping("/brand/{id}")
    public Brand queryBrandById(@PathVariable("id") Long id);*/
}
