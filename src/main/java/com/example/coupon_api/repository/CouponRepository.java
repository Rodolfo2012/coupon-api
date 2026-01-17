package com.example.coupon_api.repository;

import com.example.coupon_api.model.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, String> {

    @Query("SELECT c FROM Coupon c WHERE c.id = :id")
    Optional<Coupon> findByCodeWithDeleted(String id);
}
