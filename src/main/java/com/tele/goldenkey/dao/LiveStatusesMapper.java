package com.tele.goldenkey.dao;

import com.tele.goldenkey.domain.LiveStatuses;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface LiveStatusesMapper extends Mapper<LiveStatuses> {

    LiveStatuses findByLivedId(@Param("livedId") Integer livedId);

    Integer closeByLivedId(@Param("livedId") Integer livedId);

    Integer openByLivedId(@Param("livedId") Integer livedId);

    List<LiveStatuses> noLongerUsed();

    void updateCount(@Param("livedId")Integer id,@Param("num")Integer num);
}