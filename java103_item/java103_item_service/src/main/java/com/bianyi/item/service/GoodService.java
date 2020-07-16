package com.bianyi.item.service;

import com.bianyi.item.mapper.*;
import com.bianyi.item.pojo.*;
import com.bianyi.pojo.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class GoodService {
    @Autowired
    SpuMapper spuMapper;

    @Autowired
    CategoryService categoryService;

    @Autowired
    BrandMapper brandMapper;

    @Autowired
    SkuMapper skuMapper;

    @Autowired
    StockMapper stockMapper;

    @Autowired
    SpuDetailMapper spuDetailMapper;

    /**
     * 业务需求
     *
     * @param key
     * @param saleable 是否上架
     * @param page
     * @param rows
     * @return
     */
    public PageResult<SpuBo> findQueryAndPage(String key, Boolean saleable, Integer page, Integer rows) {
        //第一步，模糊查询
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        //进行分页操作
        PageHelper.startPage(page, rows);
        //查看是上架
        if (saleable) {
            criteria.andEqualTo("saleable", 1);
        } else {
            criteria.andEqualTo("saleable", 0);
        }
        //执行查询
        List<Spu> spus = spuMapper.selectByExample(example);
        PageInfo<Spu> info = new PageInfo(spus);
        List<SpuBo> spuBos = new ArrayList<>();

        for (Spu spu : spus) {
            SpuBo spuBo = new SpuBo();
            //执行beanCopy操作,copy共同属性的值到新的对象
            BeanUtils.copyProperties(spu, spuBo);
            List<String> names = categoryService.SelectByIdList(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            String cname = names.get(0) + "/" + names.get(1) + "/" + names.get(2);
            // 查询分类名称StringUtils.join(names, "/")
            spuBo.setCname(cname);
            // 查询品牌的名称
            spuBo.setBname(brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());
            spuBos.add(spuBo);
        }

        return new PageResult(info.getTotal(), spuBos);
    }

    /**
     * 保存spubo中的数据
     *
     * @param spuBo
     */

    public void savaSpuBo(SpuBo spuBo) {
        // 设置默认字段
        spuBo.setId(null);
        spuBo.setValid(true);
        spuBo.setSaleable(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        //首先存如spu中的数据,这里存储的是spu中的字段
        spuMapper.insertSelective(spuBo);

        //保存spuDetail中的数据
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        spuDetailMapper.insertSelective(spuDetail);
        //保存spubo中skus集合数据所封装的方法
        savaSkusAndStock(spuBo);

    }

    /**
     * 保存spubo中skus集合数据
     *
     * @param spuBo
     */
    private void savaSkusAndStock(SpuBo spuBo) {
        //首先获取spu中skus集合的字段
        List<Sku> skus = spuBo.getSkus();
        //遍历集合
        for (Sku sku : skus) {
            // 新增sku
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            //在存储list<Sku>中的数据
            skuMapper.insertSelective(sku);
            // 新增库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insertSelective(stock);
        }
    }

    /**
     * 展示修改窗口
     *
     * @param spuId
     * @return
     */
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        return spuDetail;
    }

    /**
     * 通过spuid返回一个sku的list集合
     *
     * @param spuId
     * @return
     */
    @Transactional
    public List<Sku> querySkuBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skus = skuMapper.select(sku);
        //遍历集合并在集合中将stock库存数据添加进去
        for (Sku skus1 : skus) {
            Stock stock = stockMapper.selectByPrimaryKey(skus1.getId());
            skus1.setStock(stock.getStock());
        }
        return skus;
    }

    public void updateSpuBo(SpuBo spuBo) {
        //先删除sku中库存的记录
        List<Sku> skus = querySkuBySpuId(spuBo.getId());
        for (Sku sku : skus) {
            //删除stock
            stockMapper.deleteByPrimaryKey(sku.getId());
        }
        //删除sku中的数据
        Sku sku = new Sku();
        sku.setSpuId(spuBo.getId());
        skuMapper.deleteByPrimaryKey(sku);
        //新增sku和stock
        savaSkusAndStock(spuBo);
        //更新spu
        spuMapper.updateByPrimaryKeySelective(spuBo);
        //更新spuDetail数据
        spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());
    }
}
