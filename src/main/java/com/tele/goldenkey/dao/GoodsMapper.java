package com.tele.goldenkey.dao;

import com.tele.goldenkey.domain.Goods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface GoodsMapper extends Mapper<Goods> {

    @Select(" SELECT * FROM goods WHERE  user_id=#{userId} ORDER BY id DESC ")
    List<Goods> page(@Param("userId") Integer userId);
}