package br.com.example.spring.patch.sample.service

import br.com.example.spring.patch.sample.constants.ErrorMessages.Companion.PRODUCT_NOT_FOUND
import br.com.example.spring.patch.sample.exceptions.NotFoundException
import br.com.example.spring.patch.sample.model.entities.ProductEntity
import br.com.example.spring.patch.sample.model.request.ProductPriceUpdateRequest
import br.com.example.spring.patch.sample.model.request.ProductUpdateRequest
import br.com.example.spring.patch.sample.model.response.ProductResponse
import br.com.example.spring.patch.sample.repositories.ProductRepository
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.math.BigDecimal
import java.math.BigInteger

@Service
class ProductService(
    private val repository: ProductRepository,
    private val modelMapper: ModelMapper
) {

    private val log = KotlinLogging.logger {}

    /**
     * Advantage of a well-defined DTO with the need for one-off change
     * Disadvantage if new possible data for change arises, it must be changed in the DTO
     */
    fun updateProductPrice(id: BigInteger, productPriceUpdateRequest: ProductPriceUpdateRequest) {
        val productEntity = getProductById(id)
        productEntity.price = productPriceUpdateRequest.price
        repository.save(productEntity)
    }

    /**
     * Advantage of not having to maintain your DTO, it can be extracted as useful to the project
     * Disadvantage of processing at runtime
     */
    fun updateProductUsingReflection(id: BigInteger, fields: Map<String, Any>) {
        val productEntity = getProductById(id)
        fields.forEach { (key, value) ->
            val field = ReflectionUtils.findField(ProductEntity::class.java, key)
            field?.let {
                ReflectionUtils.makeAccessible(it)
                when (it.name) {
                    PRODUCT_PRICE_ATTRIBUTE_NAME -> ReflectionUtils.setField(
                        it,
                        productEntity,
                        BigDecimal(value.toString())
                    )
                    else -> ReflectionUtils.setField(it, productEntity, value)
                }
            }
        }
        repository.save(productEntity)
    }

    /**
     * Advantage contemplates the possible alteration of several data
     * Disadvantage needed to verify which data is being informed for change, as it is flexible
     */
    fun updateProductByComparison(id: BigInteger, productUpdateRequest: ProductUpdateRequest) {
        val productEntity = getProductById(id)

        productUpdateRequest.name?.let {
            productEntity.name = productUpdateRequest.name
        }
        productUpdateRequest.description?.let {
            productEntity.description = productUpdateRequest.description
        }
        productUpdateRequest.price?.let {
            productEntity.price = productUpdateRequest.price
        }

        repository.save(productEntity)
    }

    private fun getProductById(id: BigInteger) =
        repository.findById(id) ?: throw NotFoundException(PRODUCT_NOT_FOUND).also {
            log.warn { "getProductById: product not found, productId=$id" }
        }

    fun getProduct(id: BigInteger): ProductResponse {
        val product = repository.findById(id) ?: throw NotFoundException(PRODUCT_NOT_FOUND).also {
            log.warn { "getProduct: product not found, productId=$id" }
        }
        return convertToDto(product)
    }

    private fun convertToDto(productEntity: ProductEntity): ProductResponse {
        return modelMapper.map(productEntity, ProductResponse::class.java)
    }

    companion object {
        const val PRODUCT_PRICE_ATTRIBUTE_NAME = "price"
    }
}
