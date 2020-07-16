package com.bianyi.search.service;

import com.bianyi.item.pojo.*;
import com.bianyi.search.client.BrandClient;
import com.bianyi.search.client.CategoryClient;
import com.bianyi.search.client.GoodsClient;
import com.bianyi.search.client.SpecificationClient;
import com.bianyi.search.pojo.Goods;
import com.bianyi.search.repository.GoodsRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
@Component
public class GoodsService {
    @Autowired
    BrandClient brandClient;
    @Autowired
    CategoryClient categoryClient;
    @Autowired
    GoodsClient goodsClient;
    @Autowired
    SpecificationClient specificationClient;
    @Autowired
    GoodsRepository goodsRepository;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Goods bulidGoods(Spu spu) throws IOException {
        Goods goods=new Goods();
        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        //获取分类名称
        List<String> names = categoryClient.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //获取skus集合
        List<Sku> skus = goodsClient.querySkuBySpuId(spu.getId());
        //构建保存价格的集合
        List<Long> prices=new ArrayList<>();
        List<Map<String,Object>> skuMapList =new ArrayList<>();
        //循环遍历skus,将集合中的价格存入prices集合中
        for (Sku sku : skus) {
            Map<String, Object> skuMap = new HashMap<>();
            //获取skuId
            skuMap.put("id",sku.getId());
            //获取标题
            skuMap.put("title",sku.getTitle());
            //获取显示的价格
            skuMap.put("price",sku.getPrice());
            //获取所有图片
            skuMap.put("image",StringUtils.isNotBlank(sku.getImages())?
                        StringUtils.split(sku.getImages(),","):"");
            prices.add(sku.getPrice());
            skuMapList.add(skuMap);
        }
        // 查询出所有的搜索规格参数
        List<SpecParam> params = specificationClient.queryParams(null, spu.getCid3(), null, true);
        // 查询spuDetail。获取规格参数值
        SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(spu.getId());
        // 获取通用的规格参数
        Map<Long,Object> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>() {
        });
        // 获取特殊的规格参数
        Map<Long,Object> specialSpecMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, Object>>() {
        });
        // 定义map接收{规格参数名，规格参数值}
        Map<String, Object> paramMap = new HashMap<>();
        for (SpecParam param : params) {
            // 判断是否通用规格参数
            if (param.getGeneric()){
                // 获取通用规格参数值
                String value = genericSpecMap.get(param.getId()).toString();
                // 判断是否是数值类型
                if (param.getNumeric()) {
                    // 如果是数值的话，判断该数值落在那个区间
                    value = chooseSegment(value, param);
                }
                // 把参数名和值放入结果集中
                paramMap.put(param.getName(), value);
            }else {
                paramMap.put(param.getName(), specialSpecMap.get(param.getId()));
            }
        }
        // 设置参数
        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        goods.setAll(spu.getTitle() + brand.getName() + StringUtils.join(names, " "));
        goods.setPrice(prices);
        goods.setSkus(MAPPER.writeValueAsString(skuMapList));
        goods.setSpecs(paramMap);

        return goods;
    }
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }
}
