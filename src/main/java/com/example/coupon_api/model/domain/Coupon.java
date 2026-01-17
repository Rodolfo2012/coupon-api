package com.example.coupon_api.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "coupons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE coupons SET deleted = true WHERE id = ?")
public class Coupon {
    @Id
    private String id;
    private String code;
    private String description;
    private Double discountValue;
    private LocalDate expirationDate;
    private boolean published;
    private boolean deleted = false;

    public static Coupon create(String code, String description, Double discountValue, LocalDate expirationDate) {
        String cleanCode = code.replaceAll("[^a-zA-Z0-9]", "");
        
        if (cleanCode.length() > 6) {
            cleanCode = cleanCode.substring(0, 6);
        } else if (cleanCode.length() < 6) {
             throw new IllegalArgumentException("O código deve ter pelo menos 6 caracteres alfanuméricos.");
        }

        if (discountValue < 0.5) {
            throw new IllegalArgumentException("O valor de desconto deve ser no mínimo 0.5");
        }

        if (expirationDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data de expiração não pode ser no passado");
        }

        return Coupon.builder()
                .id(UUID.randomUUID().toString())
                .code(cleanCode.toUpperCase())
                .description(description)
                .discountValue(discountValue)
                .expirationDate(expirationDate)
                .published(true)
                .build();
    }

}
