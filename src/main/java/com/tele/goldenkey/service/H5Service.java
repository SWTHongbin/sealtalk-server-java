package com.tele.goldenkey.service;

import com.tele.goldenkey.dao.GroupsMapper;
import com.tele.goldenkey.dao.LiveUserMapper;
import com.tele.goldenkey.domain.Groups;
import com.tele.goldenkey.domain.LiveUser;
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
    private final static String INNER_TXT = "%s邀请您加入%s群聊";
    private final static String SHARE_TXT = "%s邀请你试玩TelePathy,赶快来下载";

    private final LiveUserMapper liveUserMapper;
    private final GroupsMapper groupsMapper;


    public String query(Integer userId, Integer groupId) {
        log.info("userId:{},groupId:{}", userId, groupId);
        if (userId == null) return "";
        LiveUser liveUser = liveUserMapper.selectByUserId(userId);
        if (liveUser == null) return "";
        if (groupId == null) {
            return String.format(SHARE_TXT, liveUser.getName());
        }
        Groups groups = groupsMapper.selectByPrimaryKey(groupId);
        if (groups == null) return "";
        return String.format(INNER_TXT, liveUser.getName(), groups.getName());
    }
}
