package com.example.coupon_api.service;

import com.example.coupon_api.model.DTO.CouponRequest;
import com.example.coupon_api.model.response.CouponResponse;

public interface CouponService {

    CouponResponse createCoupon(CouponRequest request);
    String deleteCoupon(String code);

}
