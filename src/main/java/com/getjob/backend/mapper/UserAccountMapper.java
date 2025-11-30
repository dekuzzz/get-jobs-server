package com.getjob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.getjob.backend.model.UserAccountEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccountEntity> {
}

