package com.bianyi.item.mapper;

import com.bianyi.item.pojo.Category;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;
@Component
public interface CategoryMapper extends Mapper<Category>,SelectByIdListMapper<Category,Long> {

}
