package com.tele.goldenkey.dao;

import com.tele.goldenkey.domain.LiveStatuses;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface LiveStatusesMapper extends Mapper<LiveStatuses> {

    LiveStatuses findByLivedId(@Param("livedId") Integer livedId);

    Integer closeByLivedId(@Param("livedId") Integer livedId);

    Integer openByLivedId(@Param("livedId") Integer livedId);
}