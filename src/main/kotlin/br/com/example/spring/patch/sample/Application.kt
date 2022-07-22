package br.com.example.spring.patch.sample

import br.com.example.spring.patch.sample.constants.Products
import br.com.example.spring.patch.sample.repositories.ProductRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application(
    private val repository: ProductRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        repository.deleteAll()
        Products.productCatalog.forEach(repository::save)
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}