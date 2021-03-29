package com.tele.goldenkey.dao;

import com.tele.goldenkey.domain.LiveStatuses;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface LiveStatusesMapper extends Mapper<LiveStatuses> {

    LiveStatuses findById(@Param("livedId") Integer livedId);

    Integer closeById(@Param("livedId") Integer livedId);

    Integer openById(@Param("livedId") Integer livedId);

    List<LiveStatuses> noLongerUsed();
}