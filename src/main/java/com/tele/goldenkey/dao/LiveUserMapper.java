package com.tele.goldenkey.dao;

import com.tele.goldenkey.controller.param.LiveUserParam;
import com.tele.goldenkey.domain.LiveUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface LiveUserMapper extends Mapper<LiveUser> {

    @Delete("DELETE  FROM  live_user WHERE  liveId =#{livedId}")
    void deleteByLivedId(@Param("livedId") Integer livedId);

    @Delete("DELETE  FROM  live_user WHERE  userId =#{userId}")
    void deleteByUserId(@Param("userId") Integer userId);

    @Select(" select *  FROM  live_user WHERE  userId =#{userId} ")
    LiveUser selectByUserId(@Param("userId") Integer userId);

    @Update(" UPDATE  live_user  SET maiStatus =#{status} WHERE userId =#{userId} ")
    Integer updateMai(@Param("status") Integer status, @Param("userId") Integer userId);

    @Update(" UPDATE  live_user  SET maiPower =#{status} WHERE userId =#{userId} ")
    Integer updateMaiPower(@Param("status") Integer status, @Param("userId") Integer userId);

    @Update(" UPDATE  live_user  SET permissionSpeak =#{status} WHERE userId =#{userId} ")
    Integer updateSpeak(@Param("status") Integer status, @Param("userId") Integer userId);

    @Select(" select count(id)  from  live_user  WHERE  liveId =#{livedId} ")
    Integer countByLiveId(@Param("livedId") Integer livedId);

    @Select({"<script>",
            " SELECT  *  FROM live_user  ",
            " <where> ",
            "<if test=\"param.maiStatus != null\">",
            " AND maiStatus = #{param.maiStatus}",
            "</if>",
            "<if test=\"param.userId != null\">",
            " AND userId = #{param.userId}",
            "</if>",
            "<if test=\"param.livedId != null\">",
            " AND liveId = #{param.livedId}",
            "</if>",
            " </where> ",
            " ORDER BY createdAt asc ",
            "</script>"})
    List<LiveUser> selectByUserParam(@Param("param") LiveUserParam param);
}
