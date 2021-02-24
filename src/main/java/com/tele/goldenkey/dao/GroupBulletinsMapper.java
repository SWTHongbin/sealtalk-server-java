package com.tele.goldenkey.dao;

import com.tele.goldenkey.domain.GroupBulletins;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface GroupBulletinsMapper extends Mapper<GroupBulletins> {
    GroupBulletins getLastestGroupBulletin(@Param("groupId") Integer groupId);
}