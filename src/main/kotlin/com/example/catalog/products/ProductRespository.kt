package com.example.catalog.products

import org.springframework.data.repository.CrudRepository

interface ProductRepository : CrudRepository<Product, Int>