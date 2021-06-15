package com.tele.goldenkey.service;

import com.github.pagehelper.Page;
import com.tele.goldenkey.dao.GoodsMapper;
import com.tele.goldenkey.domain.Goods;
import com.tele.goldenkey.model.dto.PageDto;
import com.tele.goldenkey.model.dto.SearchPageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsService extends AbstractBaseService<Goods, Long> {
    private final GoodsMapper goodsMapper;

    @Override
    protected Mapper<Goods> getMapper() {
        return goodsMapper;
    }

    public PageDto<Goods> list(Integer userId, SearchPageDto searchPageDto) {
        Page<Object> page = searchPageDto.startPage();
        List<Goods> goodsList = goodsMapper.page(userId);
        return new PageDto<>(goodsList, page);
    }
}
