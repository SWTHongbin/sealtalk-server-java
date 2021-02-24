package com.tele.goldenkey.dao;

import com.tele.goldenkey.domain.GroupReceivers;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface GroupReceiversMapper extends Mapper<GroupReceivers> {


    List<GroupReceivers> selectReceiversWithList(@Param("groupId") Integer groupId,@Param("requesterId") Integer requesterId,@Param("receiverIdList") List<Integer> receiverIdList,@Param("operatorList") List<Integer> operatorList,@Param("groupReceiveType") int groupReceiveType);

    int updateReceiversWithList(@Param("requesterIdForUpdate") Integer requesterIdForUpdate,@Param("timestamp") Long timestamp,@Param("status") Integer status,@Param("groupId") Integer groupId,@Param("requesterId") Integer requesterId,@Param("receiverIdList") List<Integer> receiverIdList,@Param("operatorList") List<Integer> operatorList,@Param("groupReceiveType") int groupReceiveType);

    int insertBatch(@Param("groupReceiverList") List<GroupReceivers> groupReceiverList);

    List<GroupReceivers> selectGroupReceiversWithIncludes(@Param("userId") Integer userId);
}