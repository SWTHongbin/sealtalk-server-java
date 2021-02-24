package com.tele.goldenkey.dao;

import com.tele.goldenkey.domain.DataVersions;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface DataVersionsMapper extends Mapper<DataVersions> {

    void updateAllFriendshipVersion(@Param("userId") int userId,@Param("timestamp") long timestamp);

    void updateGroupMemberVersion(@Param("groupId") Integer groupId, @Param("timestamp") long timestamp);

    void updateGroupVersion(@Param("groupId") Integer groupId, @Param("timestamp") long timestamp);
}