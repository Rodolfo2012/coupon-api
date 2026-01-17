package com.example.coupon_api.model.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CouponTest {

    @Test
    void testCreateCouponWithValidDataShouldCreateCoupon() {
        String code = "VALID123";
        String description = "Valid Coupon";
        Double discountValue = 10.0;
        LocalDate expirationDate = LocalDate.now().plusDays(10);

        Coupon coupon = Coupon.create(code, description, discountValue, expirationDate);

        assertThat(coupon).isNotNull();
        assertThat(coupon.getId()).isNotNull();
        assertThat(coupon.getCode()).isEqualTo("VALID1");
        assertThat(coupon.getDescription()).isEqualTo(description);
        assertThat(coupon.getDiscountValue()).isEqualTo(discountValue);
        assertThat(coupon.getExpirationDate()).isEqualTo(expirationDate);
        assertThat(coupon.isPublished()).isTrue();
        assertThat(coupon.isDeleted()).isFalse();
    }

    @Test
    void testCreateCouponWithCodeLongerThan6CharsShouldTruncateCode() {
        String longCode = "THISCODEISWAYTOOLONG";

        Coupon coupon = Coupon.create(longCode, "description", 1.0, LocalDate.now().plusDays(1));

        assertThat(coupon.getCode()).hasSize(6);
        assertThat(coupon.getCode()).isEqualTo("THISCO");
    }

    @Test
    void testCreateCouponWithCodeShorterThan6CharsShouldThrowException() {
        String shortCode = "SHORT";

        assertThatThrownBy(() -> Coupon.create(shortCode, "description", 1.0, LocalDate.now().plusDays(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O código deve ter pelo menos 6 caracteres alfanuméricos.");
    }

    @Test
    void testCreateCoupon_WithNonAlphanumericCharsInCode_ShouldSanitizeCode() {
        String dirtyCode = "CODE-123!@#";

        Coupon coupon = Coupon.create(dirtyCode, "description", 1.0, LocalDate.now().plusDays(1));

        assertThat(coupon.getCode()).isEqualTo("CODE12");
    }

    @Test
    void testCreateCouponWithExactly6AlphanumericCharsShouldCreateCoupon() {
        String dirtyCode = "C-O-D-E-1-2";

        Coupon coupon = Coupon.create(dirtyCode, "description", 1.0, LocalDate.now().plusDays(1));

        assertThat(coupon.getCode()).isEqualTo("CODE12");
    }

    @Test
    void testCreateCouponWithDiscountValueLessThanMinimumShouldThrowException() {
        Double lowDiscount = 0.49;

        assertThatThrownBy(() -> Coupon.create("VALIDCODE", "description", lowDiscount, LocalDate.now().plusDays(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O valor de desconto deve ser no mínimo 0.5");
    }

    @Test
    void testCreateCouponWithMinimumDiscountValueShouldCreateCoupon() {
        Double minDiscount = 0.5;

        Coupon coupon = Coupon.create("VALIDCODE", "description", minDiscount, LocalDate.now().plusDays(1));

        assertThat(coupon.getDiscountValue()).isEqualTo(minDiscount);
    }

    @Test
    void testCreateCouponWithPastExpirationDateShouldThrowException() {
        LocalDate pastDate = LocalDate.now().minusDays(1);

        assertThatThrownBy(() -> Coupon.create("VALIDCODE", "description", 1.0, pastDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A data de expiração não pode ser no passado");
    }

    @Test
    void testCreateCouponWithTodayAsExpirationDateShouldCreateCoupon() {
        LocalDate today = LocalDate.now();

        Coupon coupon = Coupon.create("VALIDCODE", "description", 1.0, today);

        assertThat(coupon.getExpirationDate()).isEqualTo(today);
    }
}
