package com.bianyi.search;

import com.bianyi.SearchApplication;
import com.bianyi.item.pojo.*;
import com.bianyi.item.service.GoodService;
import com.bianyi.pojo.PageResult;
import com.bianyi.search.client.BrandClient;
import com.bianyi.search.client.GoodsClient;
import com.bianyi.search.pojo.Goods;
import com.bianyi.search.repository.GoodsRepository;
import com.bianyi.search.service.GoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class)
public class TestIndex  {
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    BrandClient brandClient;
    @Autowired
    GoodsService goodsService;
    @Autowired
    GoodsClient goodsClient;
    @Autowired
    GoodsRepository goodsRepository;
    @Test
    public void test() {
        /*elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);*/
        Integer page = 1;
        Integer rows = 100;
        do {
            // 分批查询spuBo
            PageResult<SpuBo> pageResult = goodsClient.findQueryAndPage(null, true, page, rows);
            // 遍历spubo集合转化为List<Goods>
            List<Goods> goodsList = pageResult.getItems().stream().map(spuBo -> {
                try {
                    return goodsService.bulidGoods((Spu) spuBo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            this.goodsRepository.saveAll(goodsList);
            // 获取当前页的数据条数，如果是最后一页，没有100条
            rows = pageResult.getItems().size();
            // 每次循环页码加1
            page++;
            int i=1;
        } while (rows == 100);
    }

    @Test
    public void test1() throws Exception{
//        Spu spu=new Spu();
//        spu.setId(1L);
//        spu.setBrandId(2L);
//        Goods goods = goodsService.bulidGoods(spu);
//        System.out.println(goods.toString());
        SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(2l);
        System.out.println(spuDetail.toString());
        System.out.println(spuDetail.toString());
        System.out.println(spuDetail.toString());
        System.out.println(spuDetail.toString());
    }
}

