package com.tele.goldenkey.dao;

import com.tele.goldenkey.domain.LiveStatuses;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface LiveStatusesMapper extends Mapper<LiveStatuses> {

    LiveStatuses findById(@Param("livedId") Long livedId);

    Integer closeById(@Param("livedId") Long livedId);

    Integer openById(@Param("livedId") Long livedId);

    List<LiveStatuses> noLongerUsed();

    @Select(" SELECT * FROM live_statuses WHERE anchorId = #{anchorId} AND `status` = 1 ORDER BY liveId DESC LIMIT 0,1")
    LiveStatuses findOnlineByAnchorId(@Param("anchorId") Integer anchorId);
}