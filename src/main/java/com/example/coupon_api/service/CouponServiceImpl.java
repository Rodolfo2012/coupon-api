package com.example.coupon_api.service;

import com.example.coupon_api.model.domain.Coupon;
import com.example.coupon_api.model.DTO.CouponRequest;
import com.example.coupon_api.model.response.CouponResponse;
import com.example.coupon_api.repository.CouponRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CouponServiceImpl implements  CouponService{

    private final CouponRepository couponRepository;

    @Override
    public CouponResponse createCoupon(CouponRequest request) {
        log.info("Iniciando criação de cupom: {}", request);
        
        Coupon coupon = Coupon.create(
                request.getCode(),
                request.getDescription(),
                request.getDiscountValue(),
                request.getExpirationDate()
        );

        couponRepository.save(coupon);
        log.info("Cupom criado com sucesso: {}", coupon);
        
        return mapToResponse(coupon);
    }

    @Override
    public String deleteCoupon(String id) {
        log.info("Iniciando deleção de cupom com código: {}", id);

        Coupon coupon = couponRepository.findByCodeWithDeleted(id)
                .orElseThrow(() -> new EntityNotFoundException("Cupom com código " + id + " não encontrado."));

        if (coupon.isDeleted()) {
            throw new IllegalStateException("O cupom com código " + id + " já foi deletado.");
        }

        couponRepository.delete(coupon);
        log.info("Cupom com código {} deletado com sucesso (soft delete).", id);
        
        return "O cupom com id " + coupon.getId() + " foi deletado com sucesso.";
    }

    private CouponResponse mapToResponse(Coupon coupon) {
        CouponResponse response = new CouponResponse();
        response.setId(coupon.getId());
        response.setCode(coupon.getCode());
        response.setDescription(coupon.getDescription());
        response.setDiscountValue(coupon.getDiscountValue());
        response.setExpirationDate(coupon.getExpirationDate());
        response.setPublished(coupon.isPublished());
        return response;
    }
}
