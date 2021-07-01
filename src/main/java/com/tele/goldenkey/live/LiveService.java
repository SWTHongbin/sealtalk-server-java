package com.tele.goldenkey.live;

import com.github.pagehelper.Page;
import com.tele.goldenkey.controller.param.LiveParam;
import com.tele.goldenkey.dao.GoodsMapper;
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
import com.tele.goldenkey.spi.agora.AgoraRecordingService;
import com.tele.goldenkey.spi.agora.RtcTokenBuilderSample;
import com.tele.goldenkey.spi.agora.RtmTokenBuilderSample;
import com.tele.goldenkey.spi.agora.media.RtcTokenBuilder;
import com.tele.goldenkey.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

import static com.tele.goldenkey.controller.LiveController.AGORA_CHANNEL_PREFIX;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiveService extends AbstractBaseService<LiveStatuses, Integer> {

    private final LiveStatusesMapper liveStatusesMapper;
    private final LiveUserMapper liveUserMapper;
    private final FriendShipManager friendShipManager;
    private final UsersMapper usersMapper;
    private final GoodsMapper goodsMapper;
    private final AgoraRecordingService agoraRecordingService;
    private final static String QI_NIU_RECORD_URL = "http://telepathytech.com/";


    @Override
    protected Mapper<LiveStatuses> getMapper() {
        return liveStatusesMapper;
    }

    public PageDto<MyLiveDto.Resp> userLiveList(MyLiveDto.Rep rep, Integer userId) throws ServiceException {
        List<Friendships> friendList = friendShipManager.getFriendList(userId);
        List<Integer> userIds = friendList.stream().map(x -> x.getUsers().getId()).collect(toList());
        userIds.add(userId);
        rep.setUserId(userIds);
        Page<Object> page = rep.startPage();
        List<MyLiveDto.Resp> resp = liveUserMapper.userLive(rep);
        return new PageDto<>(resp, page);
    }

    //房主房间操作
    public LiveTokenDto liveOption(Integer userId, Long liveId, boolean isRecorde) throws ServiceException {
        if (isRecorde) {
            agoraRecordingService.startRecording(String.valueOf(liveId));
        }
        String shareId = String.valueOf(System.currentTimeMillis()), channelName = AGORA_CHANNEL_PREFIX + liveId;
        return anchor(liveId)
                .setRtcToken(RtcTokenBuilderSample.buildRtcToken(channelName, String.valueOf(userId), RtcTokenBuilder.Role.Role_Publisher))
                .setChannelId(channelName)
                .setLivedId(liveId)
                .setRtmToken(RtmTokenBuilderSample.buildRtmToken(String.valueOf(userId)))
                .setShareUserId(shareId)
                .setShareRtcToken(RtcTokenBuilderSample.buildRtcToken(channelName, shareId, RtcTokenBuilder.Role.Role_Publisher));
    }

    private LiveTokenDto anchor(Long livedId) {
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

    public Boolean close(Long livedId) throws ServiceException {
        LiveStatuses liveStatuses = liveStatusesMapper.findById(livedId);
        if (liveStatuses.getRecorde() == 1) {
            liveStatuses.setRecordUrl(QI_NIU_RECORD_URL.concat(agoraRecordingService.stopRecording(String.valueOf(livedId))));
            liveStatusesMapper.updateByPrimaryKeySelective(liveStatuses);
        }
        return liveStatusesMapper.closeById(livedId) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long initRoom(Integer userId, LiveParam liveParam) {
        LiveStatuses liveStatuses = new LiveStatuses();
        liveStatuses.setType(liveParam.getType());
        liveStatuses.setTheme(liveParam.getTheme());
        liveStatuses.setStartTime(new Date());
        liveStatuses.setStatus(1);
        liveStatuses.setFmLink(liveParam.getFmLink());
        liveStatuses.setAnchorId(userId);
        liveStatuses.setRecorde(liveParam.getRecorde() ? 1 : 0);
        liveStatuses.setLinkMai(liveParam.getLinkMai());
        liveStatusesMapper.insertSelective(liveStatuses);
        LiveUser liveUser = convertLiveUser(getUserById(userId), liveStatuses.getLiveId());
        liveUser.setMaiPower(1);
        liveUser.setMaiStatus(1);
        liveUserMapper.insertSelective(liveUser);
        liveParam.getGoods().stream().map(x -> x.convertDao(userId)).forEach(goodsMapper::insertSelective);
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
        liveRoomDto.setGoods(goodsMapper.page(liveStatuses.getAnchorId()));
        liveRoomDto.setCount(liveUserMapper.countByLiveId(livedId));
        liveRoomDto.setAnchorId(liveStatuses.getAnchorId());
        return liveRoomDto;
    }

    public String recordUrl(Long livedId) {
        return liveStatusesMapper.findById(livedId).getRecordUrl();
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
