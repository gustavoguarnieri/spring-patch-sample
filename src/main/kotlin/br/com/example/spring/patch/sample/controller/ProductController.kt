package br.com.example.spring.patch.sample.controller

import br.com.example.spring.patch.sample.model.request.ProductPriceUpdateRequest
import br.com.example.spring.patch.sample.model.request.ProductUpdateRequest
import br.com.example.spring.patch.sample.model.response.ProductResponse
import br.com.example.spring.patch.sample.service.ProductService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigInteger
import javax.validation.constraints.Min

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService
) {

    private val log = KotlinLogging.logger {}

    @PatchMapping("/{id}")
    private fun updateProductPrice(
        @PathVariable @Min(1) id: BigInteger,
        @RequestBody productPriceUpdateRequest: ProductPriceUpdateRequest
    ) {
        log.info("updateProductPrice: updating product price=${productPriceUpdateRequest.price}")
        return productService.updateProductPrice(id, productPriceUpdateRequest).also {
            log.info("updateProductPrice: finished update product price=${productPriceUpdateRequest.price}")
        }
    }

    @PatchMapping("/reflection/{id}")
    private fun updateProductUsingReflection(
        @PathVariable @Min(1) id: BigInteger,
        @RequestBody fields: Map<String, Any>
    ) {
        log.info("updateProductUsingReflection: updating using reflection productId=$id")
        return productService.updateProductUsingReflection(id, fields).also {
            log.info("updateProductUsingReflection: finished update using reflection productId=$id")
        }
    }

    @PatchMapping("/comparison/{id}")
    private fun updateProductByComparison(
        @PathVariable @Min(1) id: BigInteger,
        @RequestBody productUpdateRequest: ProductUpdateRequest
    ) {
        log.info("updateProductByComparison: updating productId=$id")
        return productService.updateProductByComparison(id, productUpdateRequest).also {
            log.info("updateProductByComparison: finished update by comparison productId=$id")
        }
    }

    @GetMapping("/{id}")
    private fun getProduct(@PathVariable @Min(1) id: BigInteger): ProductResponse {
        log.info("getProduct: getting productId=$id")
        return productService.getProduct(id).also {
            log.info("getProduct: finished get productId=$id")
        }
    }
}
