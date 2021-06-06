package com.tele.goldenkey.dao;

import com.tele.goldenkey.domain.UserPricePackage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;

public interface UserPricePackageMapper extends Mapper<UserPricePackage> {

    @Select("SELECT  * FROM user_price_package  WHERE  userId =#{userId}")
    UserPricePackage findByUserId(@Param("userId") Integer userId);

    @Update(" UPDATE user_price_package SET videoBalance = videoBalance + #{balance} WHERE userId=#{userId} ")
    Integer optionVideoBalance(@Param("userId") Integer userId, @Param("balance") BigDecimal balance);

    @Update(" UPDATE user_price_package SET audioBalance = audioBalance + #{balance} WHERE userId=#{userId} ")
    Integer optionAudioBalance(@Param("userId") Integer userId, @Param("balance") BigDecimal balance);
}