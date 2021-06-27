package com.tele.goldenkey.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 *
 */
@Data
public class PricePackageDto {

    private final static PricePackageDto single;
    private final static long minute_seconds = 1000 * 60;

    static {
        single = new PricePackageDto();
        single.setAudio(Price.init(7D));
        single.setVideo(Price.init(28D));
    }

    /**
     * 音频
     */
    private List<Price> audio;
    /**
     * 视频
     */
    private List<Price> video;

    private PricePackageDto() {
    }

    public static PricePackageDto getInstance() {
        return single;
    }

    @Data
    @AllArgsConstructor
    public static class Price {

        public static List<Price> init(double price) {
            return IntStream.range(1, 7)
                    .mapToObj(x -> new Price(x, price, x * price, x * minute_seconds))
                    .collect(toList());
        }

        /**
         * id
         */
        private Integer id;

        /**
         * 单价
         */
        private Double price;

        /**
         * 金额
         */
        private Double amount;

        /**
         * 秒
         */
        private Long second;
    }

}
