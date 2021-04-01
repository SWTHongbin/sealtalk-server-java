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

    @Update(" UPDATE  live_user  SET maiPower =1 WHERE userId =#{userId} ")
    Integer openMaiPower(@Param("userId") Integer userId);

    @Update(" UPDATE  live_user  SET speakPower =1 WHERE userId =#{userId} ")
    Integer openSpeakPower(@Param("userId") Integer userId);

    @Update(" UPDATE  live_user  SET speakPower =0,speakStatus=0 WHERE userId =#{userId} ")
    Integer closeSpeakPower(@Param("userId") Integer userId);

    @Update(" UPDATE  live_user  SET speakStatus =#{status} WHERE userId =#{userId} ")
    Integer updateSpeech(@Param("status") Integer status, @Param("userId") Integer userId);

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
