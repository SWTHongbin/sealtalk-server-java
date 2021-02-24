package com.tele.goldenkey.dao;

import com.tele.goldenkey.domain.Friendships;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface FriendshipsMapper extends Mapper<Friendships> {


    List<Friendships> getFriendShipListWithUsers(@Param("userId") Integer userId);

    Friendships getFriendShipWithUsers(@Param("userId") Integer userId,@Param("friendId") Integer friendId,@Param("status") Integer status);

    List<Friendships> selectFriendShipListWithUsersAndVersion(@Param("userId") Integer currentUserId, @Param("version") Long version);
}