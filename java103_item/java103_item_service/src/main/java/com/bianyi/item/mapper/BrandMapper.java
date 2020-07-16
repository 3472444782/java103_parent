package com.bianyi.item.mapper;

import com.bianyi.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface BrandMapper extends Mapper<Brand> {
    @Insert("insert into tb_category_brand values(#{cid},#{bid})")
    void insertCatagoryAndBrand(@Param("cid") Long cid,@Param("bid")Long id);
    @Select("select * from tb_brand where id in (select brand_id from tb_category_brand where category_id=#{cid})")
    List<Brand> selectBrandByCid(Long cid);
}
