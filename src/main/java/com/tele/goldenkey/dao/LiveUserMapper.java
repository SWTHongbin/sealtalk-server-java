package com.tele.goldenkey.dao;

import com.tele.goldenkey.domain.LiveUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface LiveUserMapper extends Mapper<LiveUser> {

    @Delete("DELETE  FROM  live_user WHERE  liveId =#{livedId}")
    void deleteByLivedId(@Param("livedId") Integer livedId);

    @Delete("DELETE  FROM  live_user WHERE  userId =#{userId}")
    void deleteByUserId(@Param("userId") Integer userId);

    @Select(" select *  FROM  live_user WHERE  userId =#{userId} ")
    LiveUser selectByUserId(@Param("userId") Integer userId);

    @Update(" UPDATE  live_user  SET maiStatus =#{status} WHERE userId =#{userId} ")
    Integer updateMai(@Param("status") Integer status, @Param("userId") Integer userId);
}
