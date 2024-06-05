package com.example.catalog


import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = ["classpath:application-test.properties"])
@Transactional
class ProductControllerTests {


   @Autowired
   private lateinit var mockMvc: MockMvc


   @Autowired
   private lateinit var objectMapper: ObjectMapper  // Jackson ObjectMapper


   @Test
   fun testProduct() {
       // create
       val createResult = mockMvc.perform(post("/api/users")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\":\"Test Product\",\"quantity\":10}"))
               .andExpect(status().isCreated)
               .andReturn()


       val createResponseBody = createResult.response.contentAsString
       val createdProduct = objectMapper.readValue(createResponseBody, Map::class.java)


       assertEquals("Test Product", createdProduct["name"])
       assertEquals(10, createdProduct["quantity"])


       // get by id
       val getCreatedResult = mockMvc.perform(get("/api/users/${createdProduct["id"].toString()}"))
               .andExpect(status().isOk)
               .andReturn()


       val getCreateResponseBody = getCreatedResult.response.contentAsString
       val getCreatedProduct = objectMapper.readValue(getCreateResponseBody, Map::class.java)


       assertEquals(createdProduct["id"], getCreatedProduct["id"])
       assertEquals(createdProduct["name"], getCreatedProduct["name"])
       assertEquals(createdProduct["quantity"], getCreatedProduct["quantity"])


       // update
       val updateResult = mockMvc.perform(put("/api/users/${createdProduct["id"].toString()}")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\":\"Updated Test Product\",\"quantity\":5}"))
               .andExpect(status().isOk)
               .andReturn()


       val updateResponseBody = updateResult.response.contentAsString
       val updatedProduct = objectMapper.readValue(updateResponseBody, Map::class.java)
       assertEquals(createdProduct["id"], updatedProduct["id"])
       assertEquals("Updated Test Product", updatedProduct["name"])
       assertEquals(5, updatedProduct["quantity"])


       // get by id
       val getUpdatedResult = mockMvc.perform(get("/api/users/${updatedProduct["id"].toString()}"))
               .andExpect(status().isOk)
               .andReturn()


       val getUpdateResponseBody = getUpdatedResult.response.contentAsString
       val getUpdatedProduct = objectMapper.readValue(getUpdateResponseBody, Map::class.java)


       assertEquals(updatedProduct["id"], getUpdatedProduct["id"])
       assertEquals(updatedProduct["name"], getUpdatedProduct["name"])
       assertEquals(updatedProduct["quantity"], getUpdatedProduct["quantity"])


       // delete
       mockMvc.perform(delete("/api/users/${updatedProduct["id"].toString()}"))
               .andExpect(status().isNoContent)


       // get by id (should error)
       mockMvc.perform(get("/api/users/${updatedProduct["id"].toString()}"))
               .andExpect(status().isNotFound)
   }


   @Test
   fun testGetAll() {
       mockMvc.perform(post("/api/users")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\":\"A\",\"quantity\":1}"))


       mockMvc.perform(post("/api/users")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\":\"B\",\"quantity\":2}"))


       mockMvc.perform(post("/api/users")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\":\"C\",\"quantity\":3}"))


       mockMvc.perform(get("/api/users"))
               .andExpect(status().isOk)
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
   }


}
