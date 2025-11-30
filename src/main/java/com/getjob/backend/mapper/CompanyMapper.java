package com.getjob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.getjob.backend.model.CompanyEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyMapper extends BaseMapper<CompanyEntity> {
}

