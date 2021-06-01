package com.tele.goldenkey.live;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.tele.goldenkey.controller.param.LiveParam;
import com.tele.goldenkey.dao.LiveStatusesMapper;
import com.tele.goldenkey.dao.LiveUserMapper;
import com.tele.goldenkey.dao.UsersMapper;
import com.tele.goldenkey.domain.Friendships;
import com.tele.goldenkey.domain.LiveStatuses;
import com.tele.goldenkey.domain.LiveUser;
import com.tele.goldenkey.domain.Users;
import com.tele.goldenkey.dto.LiveRoomDto;
import com.tele.goldenkey.dto.LiveTokenDto;
import com.tele.goldenkey.exception.ServiceException;
import com.tele.goldenkey.manager.FriendShipManager;
import com.tele.goldenkey.model.dto.MyLiveDto;
import com.tele.goldenkey.model.dto.PageDto;
import com.tele.goldenkey.service.AbstractBaseService;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class LiveService extends AbstractBaseService<LiveStatuses, Integer> {

    private final LiveStatusesMapper liveStatusesMapper;
    private final LiveUserMapper liveUserMapper;
    private final FriendShipManager friendShipManager;
    private final UsersMapper usersMapper;

    @Override
    protected Mapper<LiveStatuses> getMapper() {
        return liveStatusesMapper;
    }

    public PageDto<MyLiveDto.Resp> userLiveList(MyLiveDto.Rep rep, Integer userId) throws ServiceException {
        List<Friendships> friendList = friendShipManager.getFriendList(userId);
        List<Integer> userIds = friendList.stream().map(Friendships::getUserId).collect(toList());
        userIds.add(userId);
        rep.setUserId(userIds);
        Page<Object> page = rep.startPage();
        List<MyLiveDto.Resp> resp = liveUserMapper.userLive(rep);
        return new PageDto<>(resp, page);
    }

    public LiveTokenDto anchor(Long livedId) {
        LiveTokenDto liveTokenDto = new LiveTokenDto();
        liveTokenDto.setRoomDto(room(livedId));
        liveTokenDto.setLivedId(livedId);
        liveTokenDto.setUserId(liveTokenDto.getRoomDto().getAnchorId());
        return liveTokenDto;
    }

    public Boolean isOpen(Long livedId) {
        LiveStatuses liveStatuses = liveStatusesMapper.findById(livedId);
        return liveStatuses != null && liveStatuses.getStatus() == 1;
    }

    public Boolean close(Long livedId) {
        return liveStatusesMapper.closeById(livedId) > 0;
    }

    public Long initRoom(Integer userId, LiveParam liveParam) {
        LiveStatuses liveStatuses = new LiveStatuses();
        liveStatuses.setType(liveParam.getType());
        liveStatuses.setTheme(liveParam.getTheme());
        liveStatuses.setStartTime(new Date());
        liveStatuses.setStatus(1);
        liveStatuses.setFmLink(liveParam.getFmLink());
        liveStatuses.setGoods(JSONObject.toJSONString(liveParam.getGoods()));
        liveStatuses.setAnchorId(userId);
        liveStatuses.setLinkMai(liveParam.getLinkMai());
        liveStatusesMapper.insertSelective(liveStatuses);
        LiveUser liveUser = convertLiveUser(getUserById(userId), liveStatuses.getLiveId());
        liveUser.setMaiPower(1);
        liveUser.setMaiStatus(1);
        liveUserMapper.insertSelective(liveUser);
        return liveStatuses.getLiveId();
    }

    public Long leave(Integer userId) throws ServiceException {
        LiveUser liveUser = liveUserMapper.selectByUserId(userId);
        ValidateUtils.notNull(liveUser);
        liveUserMapper.deleteByUserId(userId);
        return liveUser.getLiveId();
    }

    public void join(Integer userId, Long livedId) {
        liveUserMapper.deleteByUserId(userId);
        liveUserMapper.insertSelective(convertLiveUser(getUserById(userId), livedId));
    }

    public LiveRoomDto room(Long livedId) {
        LiveStatuses liveStatuses = liveStatusesMapper.findById(livedId);
        if (liveStatuses == null) return null;
        LiveRoomDto liveRoomDto = new LiveRoomDto();
        liveRoomDto.setType(liveStatuses.getType());
        liveRoomDto.setIsOpen(liveStatuses.getStatus() == 1);
        long timestamp = liveRoomDto.getIsOpen() ? liveStatuses.getStartTime().getTime() : liveStatuses.getUpdatedAt().getTime() - liveStatuses.getStartTime().getTime();
        liveRoomDto.setTimestamp(timestamp);
        liveRoomDto.setTheme(liveStatuses.getTheme());
        liveRoomDto.setLinkMai(liveStatuses.getLinkMai());
        liveRoomDto.setFmLink(liveStatuses.getFmLink());
        liveRoomDto.setGoods(JSONObject.parseArray(liveStatuses.getGoods(), LiveParam.Goods.class));
        liveRoomDto.setCount(liveUserMapper.countByLiveId(livedId));
        liveRoomDto.setAnchorId(liveStatuses.getAnchorId());
        return liveRoomDto;
    }

    private Users getUserById(Integer id) {
        return usersMapper.selectByPrimaryKey(id);
    }

    private LiveUser convertLiveUser(Users users, Long livedId) {
        LiveUser liveUser = new LiveUser();
        liveUser.setUserId(users.getId());
        liveUser.setPhone(users.getPhone());
        liveUser.setLiveId(livedId);
        liveUser.setName(users.getNickname());
        liveUser.setPortraitUri(users.getPortraitUri());
        return liveUser;
    }


}
