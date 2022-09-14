package com.amine.crud_bank.datasource.network

import com.amine.crud_bank.datasource.BankDataSource
import com.amine.crud_bank.model.Bank
import com.amine.crud_bank.model.BankList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.io.IOException

@Repository("network")
class NetworkDataSource(
    @Autowired private val restTemplate: RestTemplate
): BankDataSource {
    override fun retrieveBanks(): Collection<Bank> {
        TODO("Not yet implemented")
    }

    /*override fun retrieveBanks(): Collection<Bank> {
        val bankList = restTemplate.getForEntity<BankList>("http://54.193.31.159/banks")
        return bankList.body?.results ?: throw IOException("Could not fetch banks from the network")
    }*/

    override fun retrieveBank(accountNumber: String): Bank {
        TODO("Not yet implemented")
    }

    override fun createBank(bank: Bank): Bank {
        TODO("Not yet implemented")
    }

    override fun updateBank(bank: Bank): Bank {
        TODO("Not yet implemented")
    }

    override fun deleteBank(accountNumber: String) {
        TODO("Not yet implemented")
    }
}