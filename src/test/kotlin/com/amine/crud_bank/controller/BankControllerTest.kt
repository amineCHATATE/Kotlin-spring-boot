package com.amine.crud_bank.controller

import com.amine.crud_bank.model.Bank
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor (
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

    private val BASE_URL = "/api/v1/banks"

    @Nested
    @DisplayName("GET /api/v1/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBanks {

        @Test
        fun `should return all banks`() {
            // when / then
            mockMvc.get(BASE_URL)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].account_number") {
                        value("123")
                    }
                }
        }
    }

    @Nested
    @DisplayName("GET /api/v1/banks/{accountNumber}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBank {

        @Test
        fun `should return the bank with the given account number`() {
            // given
            val accountNumber = 123

            // when / then
            mockMvc.get("$BASE_URL/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.trust") { value("1.0") }
                    jsonPath("$.default_transaction_fee") { value("4") }
                }
        }
    }

    @Test
    fun `should return Not Found if the account number does not exist`() {
        // given
        val accountNumber = "does_not_exist"

        // when / then
        mockMvc.get("$BASE_URL/$accountNumber")
            .andDo { print() }
            .andExpect { status { isNotFound() } }

    }
    
    @Nested
    @DisplayName("POST /api/v1/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostNewBank {
    
        @Test
        fun `should add the new bank`() {
            // given
            val newBank = Bank("aaa", 6.5, 8)
            
            // when
            val performMock = mockMvc.post(BASE_URL) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }

            // then
            performMock
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.account_number") {value("aaa") }
                    jsonPath("$.trust") {value("6.5") }
                    jsonPath("$.default_transaction_fee") {value("8") }
                }
        }
            
    }
    
    @Test
    fun `should return BAD REQUEST if bank with given account number already exists`() {
        // given
        val invalidBank = Bank("123", 1.0, 1)
        
        // when
        val performMock = mockMvc.post(BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(invalidBank)
        }
        
        // then
        performMock
            .andDo { print() }
            .andExpect {
                status { isBadRequest() }
            }

    }
    
    @Nested
    @DisplayName("PATCH /api/v1/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchExistingBank {
    
        @Test
        fun `should update an existing bank`() {
            // given
            val updatesBank = Bank("123", 1.0, 100)

            // when
            val performMock = mockMvc.patch(BASE_URL) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatesBank)
            }

            // then
            performMock
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(updatesBank))
                    }
                }

            mockMvc.get("$BASE_URL/${updatesBank.accountNumber}")
                .andExpect { content { json(objectMapper.writeValueAsString(updatesBank)) } }
        }
        
        @Test
        fun `should return BAD REQUEST if no bank with given account number exists`() {
            // given
            val invalidBank = Bank("does_not_exist", 0.0, 0)

            // when
            val performMock = mockMvc.patch(BASE_URL) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }

            // then
            performMock
                .andDo { print() }
                .andExpect {
                    status { isNotFound() } 
                }
        }
            
    }
    
    @Nested
    @DisplayName("DELETE /api/v1/banks")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeletingExistingBank {
    
        @Test
        @DirtiesContext
        fun `should delete the bank with the given account number`() {
            // given
            val accountNumber = 123
            
            // when
            val performMock = mockMvc.delete("$BASE_URL/$accountNumber")
            
            // then
            performMock.andDo { print() }
                .andExpect { status { isNoContent() } }

            mockMvc.get("$BASE_URL/$accountNumber")
                .andExpect { status { isNotFound() } }
        }

        @Test
        fun `should return NOT FOUND if no bank with given account number exists`() {
            // given
            val invalidAccountNumber = "does_not_exist"

            // when/then
            mockMvc.delete("$BASE_URL/$invalidAccountNumber")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
            
    }
}