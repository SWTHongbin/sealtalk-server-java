package com.tele.goldenkey.enums;

import com.tele.goldenkey.constant.ErrorCode;
import com.tele.goldenkey.exception.ServiceException;
import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * @date 2021年06月01日 14:46
 */
@AllArgsConstructor
public enum SkuType {
    audio(1, "音频"),
    video(2, "视频");

    public Integer code;

    public String desc;

    public static SkuType byCodeOf(Integer code) throws ServiceException {
        return Arrays.stream(SkuType.values()).filter(x -> x.code.equals(code)).findAny().orElseThrow(() -> new ServiceException(ErrorCode.PARAM_ERROR));
    }
}
