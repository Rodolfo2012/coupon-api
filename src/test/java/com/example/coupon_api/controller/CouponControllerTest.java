package com.example.coupon_api.controller;

import com.example.coupon_api.exception.GlobalExceptionHandler;
import com.example.coupon_api.model.DTO.CouponRequest;
import com.example.coupon_api.model.domain.Coupon;
import com.example.coupon_api.repository.CouponRepository;
import com.example.coupon_api.service.CouponService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CouponRepository couponRepository;

    @Mock
    private CouponService couponService;

    @AfterEach
    void tearDown() {
        couponRepository.deleteAll();
    }

    @Nested
    @DisplayName("POST /v1/coupons")
    class CreateCouponTests {

        @Test
        @DisplayName("Deve retornar 201 Created e truncar o código ao criar cupom com sucesso")
        void shouldReturn201WhenCouponIsCreatedSuccessfully() throws Exception {
            CouponRequest request = new CouponRequest();
            request.setCode("PROMO12345");
            request.setDescription("Super Promo");
            request.setDiscountValue(10.0);
            request.setExpirationDate(LocalDate.now().plusDays(10));

            mockMvc.perform(post("/v1/coupons")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", notNullValue()))
                    .andExpect(jsonPath("$.code", is("PROMO1")))
                    .andExpect(jsonPath("$.published", is(true)));
        }

        @Test
        @DisplayName("Deve retornar 400 Bad Request para dados de entrada inválidos (código nulo)")
        void shouldReturn400WhenInputIsInvalid() throws Exception {
            CouponRequest request = new CouponRequest();
            request.setDescription("Incompleto");
            request.setDiscountValue(10.0);
            request.setExpirationDate(LocalDate.now().plusDays(10));

            mockMvc.perform(post("/v1/coupons")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Validation Failed")))
                    .andExpect(jsonPath("$.validationErrors.code", is("O code não pode ser nulo ou vazio")));
        }

        @Test
        @DisplayName("Deve retornar 400 Bad Request para violação de regra de negócio (desconto baixo)")
        void shouldReturn400WhenBusinessRuleIsViolated() throws Exception {
            CouponRequest request = new CouponRequest();
            request.setCode("PROMO123");
            request.setDescription("Desconto baixo");
            request.setDiscountValue(0.4);

            mockMvc.perform(post("/v1/coupons")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Validation Failed")))
                    .andExpect(jsonPath("$.validationErrors.discountValue", is("O valor de desconto deve ser no mínimo 0.5")));
        }
    }

    @Nested
    @DisplayName("DELETE /v1/coupons/{code}")
    class DeleteCouponTests {

        @Test
        @DisplayName("Deve retornar 200 OK e mensagem de sucesso ao deletar cupom")
        void shouldReturn200WhenCouponIsDeletedSuccessfully() throws Exception {

            Coupon coupon = Coupon.create("TESTCODE", "Para deletar", 10.0, LocalDate.now().plusDays(5));
            Coupon coupon1 = couponRepository.save(coupon);

            mockMvc.perform(delete("/v1/coupons/{code}", coupon1.getId()))
                    .andExpect(status().isOk())
                    .andExpect(content().string("O cupom com id " + coupon.getId() + " foi deletado com sucesso."));
        }

        @Test
        @DisplayName("Deve retornar 404 Not Found para cupom que não existe")
        void shouldReturn404WhenCouponNotFound() throws Exception {
            mockMvc.perform(delete("/v1/coupons/{code}", "NAOEXI"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error", is("Not Found")))
                    .andExpect(jsonPath("$.message", is("Cupom com código NAOEXI não encontrado.")));
        }

        @Test
        @DisplayName("Deve retornar 409 Conflict ao tentar deletar um cupom já deletado")
        void shouldReturn409WhenCouponIsAlreadyDeleted() throws Exception {
            Coupon coupon = Coupon.create("TESTCODE", "Já deletado", 10.0, LocalDate.now().plusDays(5));
            Coupon coupon1 = couponRepository.save(coupon);

            mockMvc.perform(delete("/v1/coupons/{code}", coupon1.getId())).andExpect(status().isOk());

            mockMvc.perform(delete("/v1/coupons/{code}", coupon1.getId()))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.error", is("Conflict")))
                    .andExpect(jsonPath("$.message", is("O cupom com código " + coupon1.getId() + " já foi deletado.")));
        }
    }
    @Test
    @DisplayName("Deve retornar 400 Bad Request quando o serviço lança IllegalArgumentException Quando algum campo é invalido")
    void shouldReturn400WhenServiceThrowsIllegalArgumentException() throws Exception {
        CouponRequest request = new CouponRequest();
        request.setCode("SHORT");
        request.setDescription("Short code");
        request.setDiscountValue(10.0);
        request.setExpirationDate(LocalDate.now().plusDays(10));

        String errorMessage = "A entrada fornecida é inválida.";
        when(couponService.createCoupon(any(CouponRequest.class))).thenThrow(new IllegalArgumentException(errorMessage));

        mockMvc.perform(post("/v1/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Validation Failed")))
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }
}
