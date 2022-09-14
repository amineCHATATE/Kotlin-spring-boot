package com.amine.crud_bank.controller

import com.amine.crud_bank.model.Bank
import com.amine.crud_bank.service.BankService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/banks")
class BankController (private val bankService: BankService) {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(noSuchElementException: NoSuchElementException): ResponseEntity<String>
        = ResponseEntity(noSuchElementException.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleNotFound(illegalArgumentException: IllegalArgumentException): ResponseEntity<String>
            = ResponseEntity(illegalArgumentException.message, HttpStatus.BAD_REQUEST)

    @GetMapping
    fun getBanks(): Collection<Bank> = bankService.getBanks()

    @GetMapping("/{id}")
    fun getBank(@PathVariable(name = "id") accountNumber: String): Bank = bankService.getBank(accountNumber)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBank(@RequestBody bank: Bank): Bank = bankService.addBank(bank)

    @PatchMapping
    fun putBank(@RequestBody bank: Bank): Bank = bankService.updateBank(bank)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBank(@PathVariable(name = "id") accountNumber: String): Unit = bankService.deleteBank(accountNumber)
}