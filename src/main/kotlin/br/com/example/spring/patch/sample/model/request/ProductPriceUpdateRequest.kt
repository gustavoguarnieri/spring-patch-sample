package br.com.example.spring.patch.sample.model.request

import java.math.BigDecimal
import javax.validation.constraints.Min

data class ProductPriceUpdateRequest(
    @Min(1L) var price: BigDecimal
)
