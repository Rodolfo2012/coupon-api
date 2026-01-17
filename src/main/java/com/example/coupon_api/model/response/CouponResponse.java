package com.example.coupon_api.model.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CouponResponse {
    private String id;
    private String code;
    private String description;
    private Double discountValue;
    private LocalDate expirationDate;
    private boolean published;
}
