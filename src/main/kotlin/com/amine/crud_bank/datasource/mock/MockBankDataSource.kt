package com.amine.crud_bank.datasource.mock

import com.amine.crud_bank.datasource.BankDataSource
import com.amine.crud_bank.model.Bank
import org.springframework.stereotype.Repository

@Repository("mock")
class MockBankDataSource: BankDataSource {

    val banks = mutableListOf<Bank>(
        Bank("123", 1.0, 4),
        Bank("875", 10.0, 18),
        Bank("968", 8.0, 6),
    )

    override fun retrieveBanks(): Collection<Bank> = banks

    override fun retrieveBank(accountNumber: String): Bank {
        return  banks.first { it.accountNumber == accountNumber }
    }

    override fun createBank(bank: Bank): Bank {
        if (banks.any{ it.accountNumber == bank.accountNumber }){
            throw IllegalArgumentException("Bank with account number ${bank.accountNumber} already exists.!")
        }
        banks.add(bank)
        return bank
    }

    override fun updateBank(bank: Bank): Bank {
        val currentBank = banks.firstOrNull { it.accountNumber == bank.accountNumber } ?: throw NoSuchElementException("Bank with account number ${bank.accountNumber} does not exist!")
        banks.remove(currentBank)
        banks.add(bank)
        return bank
    }

    override fun deleteBank(accountNumber: String): Unit {
        val currentBank = banks.firstOrNull { it.accountNumber == accountNumber }
            ?: throw NoSuchElementException("Bank with account number ${accountNumber} does not exist!")
        banks.remove(currentBank)
    }
}