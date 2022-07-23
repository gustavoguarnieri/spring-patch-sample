package br.com.example.spring.patch.sample.model.request

import java.math.BigDecimal

data class ProductUpdateRequest(
    val name: String?,
    val description: String?,
    val price: BigDecimal?
)
