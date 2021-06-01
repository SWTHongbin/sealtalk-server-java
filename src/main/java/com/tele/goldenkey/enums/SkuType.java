package com.tele.goldenkey.enums;

import lombok.AllArgsConstructor;

/**
 * @date 2021年06月01日 14:46
 */
@AllArgsConstructor
public enum SkuType {
    audio(1, "音频"),
    video(2, "视频");

    public Integer code;

    public String desc;
}
