package com.tele.goldenkey.controller;

import com.tele.goldenkey.controller.param.GoodsParam;
import com.tele.goldenkey.domain.Goods;
import com.tele.goldenkey.model.dto.PageDto;
import com.tele.goldenkey.model.dto.SearchPageDto;
import com.tele.goldenkey.model.response.APIResult;
import com.tele.goldenkey.model.response.APIResultWrap;
import com.tele.goldenkey.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 商品组件
 */
@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController extends BaseController {

    private final GoodsService goodsService;

    /**
     * 商品列表
     *
     * @param searchPageDto
     * @return
     */
    @GetMapping("list")
    public APIResult<PageDto<Goods>> list(SearchPageDto searchPageDto) {
        Integer userId = super.getCurrentUserId();
        return APIResultWrap.ok(goodsService.list(userId, searchPageDto));
    }

    /**
     * 商品删除
     *
     * @param id
     * @return
     */
    @PostMapping("del/{id}")
    public APIResult<Boolean> del(@PathVariable Long id) {
        Integer userId = super.getCurrentUserId();
        Goods goods = new Goods();
        goods.setId(id);
        goods.setUserId(userId);
        return APIResultWrap.ok(goodsService.delete(goods) > 0);
    }

    /**
     * 添加商品
     *
     * @param goodsParam
     * @return
     */
    @PostMapping("add")
    public APIResult<Boolean> add(@RequestBody @Validated GoodsParam goodsParam) {
        Goods goods = goodsParam.convertDao();
        goods.setUserId(super.getCurrentUserId());
        return APIResultWrap.ok(goodsService.saveSelective(goods) > 0);
    }
}
