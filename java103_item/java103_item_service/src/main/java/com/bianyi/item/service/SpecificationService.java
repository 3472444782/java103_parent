package com.bianyi.item.service;

import com.bianyi.item.mapper.SpecGroupMapper;
import com.bianyi.item.mapper.SpecParamMapper;
import com.bianyi.item.mapper.SpuDetailMapper;
import com.bianyi.item.pojo.SpecGroup;
import com.bianyi.item.pojo.SpecParam;
import com.bianyi.item.pojo.SpuBo;
import com.bianyi.item.pojo.SpuDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SpecificationService {

    @Autowired
    SpecGroupMapper specGroupMapper;

    @Autowired
    SpecParamMapper specParamMapper;


    SpecParam specParam = new SpecParam();
    SpecGroup specGroup = new SpecGroup();

    public List<SpecGroup> queryGroupsByCid(Long cid) {

        specGroup.setCid(cid);
        List<SpecGroup> select = specGroupMapper.select(specGroup);
        return select;
    }

    public List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        specParam.setCid(cid);
        specParam.setGroupId(gid);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);
        return specParamMapper.select(specParam);
    }


}
