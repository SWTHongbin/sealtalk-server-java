package com.tele.goldenkey.dao;

import com.tele.goldenkey.domain.LiveStatuses;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDateTime;
import java.util.List;

public interface LiveStatusesMapper extends Mapper<LiveStatuses> {

    @Select(" SELECT liveId FROM live_statuses WHERE `status` = 1 AND pingTime BETWEEN #{startTime} and #{endTime} ORDER BY liveId DESC LIMIT 0,1000")
    List<Long> openNoPingRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}