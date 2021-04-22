package com.tele.goldenkey.service;

import com.tele.goldenkey.dao.GroupsMapper;
import com.tele.goldenkey.dao.UsersMapper;
import com.tele.goldenkey.domain.Groups;
import com.tele.goldenkey.domain.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lihongbin
 * @date 2021年04月20日 23:54
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class H5Service {
    private final static String USER_GROUP_TXT = "%s邀请您加入%s群聊";
    private final static String USER_TXT = "%s邀请你试玩TelePathy,赶快来下载";

    private final UsersMapper usersMapper;
    private final GroupsMapper groupsMapper;


    public String query(Integer userId, Integer groupId) {
        log.info("userId:{},groupId:{}", userId, groupId);
        if (userId == null) return "";
        Users users = usersMapper.selectByPrimaryKey(userId);
        if (users == null) return "";
        if (groupId == null) {
            return String.format(USER_TXT, users.getNickname());
        }
        Groups groups = groupsMapper.selectByPrimaryKey(groupId);
        if (groups == null) return "";
        return String.format(USER_GROUP_TXT, users.getNickname(), groups.getName());
    }
}
