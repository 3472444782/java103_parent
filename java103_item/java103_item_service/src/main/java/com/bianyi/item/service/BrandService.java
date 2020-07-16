package com.bianyi.item.service;

import com.bianyi.item.mapper.BrandMapper;
import com.bianyi.item.pojo.Brand;
import com.bianyi.item.pojo.Category;
import com.bianyi.pojo.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

@Service
public class BrandService {
    /**
     * pageResult.setItems();// 当前页数据
     * pageResult.setTotal();// 总页数
     * pageResult.setTotalPage();// 总条数
     */
    @Autowired
    BrandMapper brandMapper;

    public PageResult<Brand> findBrandByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        //初始化一个条件对象
        Example example = new Example(Brand.class);
        //添加一个模糊查询的条件
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {//相当于select * from where name like "%key%"
            criteria.andLike("name", "%" + key + "%");
            criteria.orEqualTo("letter", key);
        }
        PageHelper.startPage(page, rows);
        //添加升降序
        if (StringUtils.isNotBlank(sortBy)) {
            //相当于select * from where id order by desc"
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }
        List<Brand> brands = brandMapper.selectByExample(example);
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        PageResult<Brand> pageResult = new PageResult(pageInfo.getTotal(), pageInfo.getList());

        return pageResult;
    }

    public void addBrand(Brand brand, List<Long> cids) {
        //新增品牌
        brandMapper.insertSelective(brand);
        //新增中间表
        for (Long cid : cids) {
            brandMapper.insertCatagoryAndBrand(cid,brand.getId());
        }
    }

    public List<Brand> findBrandByCid(Long cid) {
        List<Brand> list = brandMapper.selectBrandByCid(cid);
        return list;
    }

    public Brand queryBrandById(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }
}
