package com.example.coupon_api.controller;

import com.example.coupon_api.model.DTO.CouponRequest;
import com.example.coupon_api.model.response.CouponResponse;
import com.example.coupon_api.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@Validated
public class CouponControllerImpl implements CouponController{

    private final CouponService couponService;

    @Override
    public ResponseEntity<CouponResponse> createCoupon(@Valid CouponRequest request) {
        CouponResponse response = couponService.createCoupon(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<String> deleteCoupon(String code) {
        String responseMessage = couponService.deleteCoupon(code);
        return ResponseEntity.ok(responseMessage);
    }
}
