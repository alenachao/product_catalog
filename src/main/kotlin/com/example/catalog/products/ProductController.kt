package com.example.catalog.products


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
class ProductController(@Autowired private val productRepository: ProductRepository) {

    @GetMapping("")
    fun getAllProducts(): List<Product> =
        productRepository.findAll().toList()

    @PostMapping("")
    fun createProduct(@RequestBody product: Product): ResponseEntity<Product> {
        val createdProduct = productRepository.save(product)
        return ResponseEntity(createdProduct, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable("id") productId: Int): ResponseEntity<Product> {
        val product = productRepository.findById(productId).orElse(null)
        return if (product != null) ResponseEntity(product, HttpStatus.OK)
               else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{id}")
    fun updateProductById(@PathVariable("id") productId: Int, @RequestBody product: Product): ResponseEntity<Product> {

        val existingProduct = productRepository.findById(productId).orElse(null)

        if (existingProduct == null) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        val updatedProduct = existingProduct.copy(name = product.name, quantity = product.quantity)
        productRepository.save(updatedProduct)
        return ResponseEntity(updatedProduct, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteProductById(@PathVariable("id") productId: Int): ResponseEntity<Product> {
        if (!productRepository.existsById(productId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        productRepository.deleteById(productId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}