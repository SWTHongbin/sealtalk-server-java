package com.tele.goldenkey.dto;

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
    public static class Price {

        private final static long minute_seconds = 1000 * 60;

        public static List<Price> init(double price) {
            return IntStream.range(1, 7)
                    .mapToObj(x -> new Price(price, x * price, x * minute_seconds))
                    .collect(toList());
        }

        public Price(Double price, Double amount, Long second) {
            this.price = price;
            this.amount = amount;
            this.second = second;
        }

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
