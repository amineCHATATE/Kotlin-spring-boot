package com.amine.crud_bank.service

import com.amine.crud_bank.datasource.BankDataSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BankServiceTest {

    private val bankDataSource: BankDataSource = mockk(relaxed = true)
    private val bankService = BankService(bankDataSource)

    @Test
    fun `should call its data source to retrieve banks`() {
        // given
        // every { bankDataSource.retrieveBanks() } returns emptyList()

        // when
        bankService.getBanks()

        // then
        verify(exactly = 1) { bankDataSource.retrieveBanks() }
    }
}