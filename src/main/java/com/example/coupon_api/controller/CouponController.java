package com.example.coupon_api.controller;

import com.example.coupon_api.model.DTO.CouponRequest;
import com.example.coupon_api.model.response.CouponResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("v1/coupons")
public interface CouponController {

    @Operation(summary = "Create a new coupon", description = "Creates a new coupon with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Coupon created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    ResponseEntity<CouponResponse> createCoupon(@RequestBody @Valid CouponRequest request);

    @Operation(summary = "Delete a coupon", description = "Deletes an existing coupon by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    @DeleteMapping("/{code}")
    ResponseEntity<String> deleteCoupon(@PathVariable String code);
}
