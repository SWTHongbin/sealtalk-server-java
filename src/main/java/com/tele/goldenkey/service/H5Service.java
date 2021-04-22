package com.tele.goldenkey.service;

import com.tele.goldenkey.dao.LiveStatusesMapper;
import com.tele.goldenkey.dao.LiveUserMapper;
import com.tele.goldenkey.domain.LiveStatuses;
import com.tele.goldenkey.domain.LiveUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author lihongbin
 * @date 2021年04月20日 23:54
 */
@Service
@RequiredArgsConstructor
public class H5Service {
    private final static String INNER_TXT = "%s邀请您加入%s群聊";
    private final static String SHARE_TXT = "%s邀请你试玩TelePathy,赶快来下载";

    private final LiveUserMapper liveUserMapper;
    private final LiveStatusesMapper liveStatusesMapper;


    public String query(Integer groupId, Integer userId) {
        if (groupId == null || userId == null) return "";
        LiveUser liveUser = liveUserMapper.selectByUserId(userId);
        if (liveUser == null) return "";
        LiveStatuses liveStatuses = liveStatusesMapper.findById(groupId);
        if (liveStatuses == null) return "";
        return String.format(INNER_TXT, liveUser.getName(), liveStatuses.getTheme());
    }

    public String share(int userId) {
        LiveUser liveUser = liveUserMapper.selectByUserId(userId);
        if (liveUser == null) return "";
        return String.format(SHARE_TXT, liveUser.getName());
    }
}
