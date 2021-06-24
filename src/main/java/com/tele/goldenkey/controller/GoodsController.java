package com.tele.goldenkey.controller;

import com.tele.goldenkey.controller.param.DelGoodsParam;
import com.tele.goldenkey.controller.param.GoodsParam;
import com.tele.goldenkey.domain.Goods;
import com.tele.goldenkey.model.dto.PageDto;
import com.tele.goldenkey.model.dto.SearchPageDto;
import com.tele.goldenkey.model.response.APIResult;
import com.tele.goldenkey.model.response.APIResultWrap;
import com.tele.goldenkey.service.GoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 商品相关
 */
@Slf4j
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
        Integer userId = getCurrentUserId();
        log.info("商品列表 用户id:{}", userId);
        return APIResultWrap.ok(goodsService.list(userId, searchPageDto));
    }

    /**
     * 商品删除
     *
     * @param id
     * @return
     */
    @PostMapping("del/{id}")
    public APIResult<Boolean> delById(@PathVariable Long id) {
        return APIResultWrap.ok(goodsService.deleteByPrimaryKey(id) > 0);
    }

    /**
     * 批量删除
     *
     * @return
     */
    @PostMapping("del")
    public APIResult<Boolean> delByIds(@RequestBody @Validated DelGoodsParam delGoodsParam) {
        delGoodsParam.getIds().forEach(goodsService::deleteByPrimaryKey);
        return APIResultWrap.ok(true);
    }

    /**
     * 删除用户下所有商品
     *
     * @return
     */
    @PostMapping("del-all")
    public APIResult<Boolean> delAllByUserId() {
        Goods goods = new Goods();
        goods.setUserId(getCurrentUserId());
        goodsService.delete(goods);
        log.info("用户id:{}手动处理商品", getCurrentUserId());
        return APIResultWrap.ok(true);
    }

    /**
     * 添加商品
     *
     * @param goodsParam
     * @return
     */
    @PostMapping("add")
    public APIResult<Boolean> add(@RequestBody @Validated GoodsParam goodsParam) {
        log.info("用户:{}添加商品信息：{}", getCurrentUserId(), goodsParam);
        return APIResultWrap.ok(goodsService.saveSelective(goodsParam.convertDao(getCurrentUserId())) > 0);
    }
}
