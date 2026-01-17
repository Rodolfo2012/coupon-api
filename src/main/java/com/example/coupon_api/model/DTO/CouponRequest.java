package com.example.coupon_api.model.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CouponRequest {

    @NotBlank(message = "O code não pode ser nulo ou vazio")
    @Size(min = 6, message = "O código deve ter no mínimo 6 caracteres alfanuméricos")
    private String code;
    
    @NotBlank(message = "A description não pode ser nula ou vazia")
    private String description;
    
    @NotNull(message = "O discountValue não pode ser nulo")
    @DecimalMin(value = "0.5", message = "O valor de desconto deve ser no mínimo 0.5")
    private Double discountValue;
    
    @NotNull(message = "A expirationDate não pode ser nula")
    @FutureOrPresent(message = "A data de expiração não pode ser no passado")
    private LocalDate expirationDate;

}
