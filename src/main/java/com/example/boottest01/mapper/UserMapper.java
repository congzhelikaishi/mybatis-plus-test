package com.example.boottest01.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.boottest01.dto.User;
import org.apache.ibatis.annotations.Mapper;

// 在对应的Mapper上继承基本的类BaseMapper
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 所有的CRUD已经编写完成
    // 不需要配置xml

}
