package com.leniolabs.challenge.controller;

import com.leniolabs.challenge.calculator.FeeCalculatorIF;
import com.leniolabs.challenge.calculator.factory.FeeCalculatorFactory;
import com.leniolabs.challenge.model.Account;
import com.leniolabs.challenge.service.AccounServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/lenio-challenge/account/v1")
public class AccountController {

    @Autowired
    private AccounServiceIF accountControllerService;

    @Autowired
    private FeeCalculatorFactory feeCalculatorFactory;

    @PostMapping(value = "/create")
    public ResponseEntity<String> createAccount(@RequestBody Account account) {
        String accountId = accountControllerService.createAccount(account);
        return ResponseEntity.ok(accountId);
    }

    @GetMapping(value = "/calculate-fee/{accountId}")
    public ResponseEntity<Double> calculateFee(@PathVariable String accountId) throws Exception {
        Optional<Account> optionalAccount = accountControllerService.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            String accountType = account.getAccountType();
            FeeCalculatorIF feeCalculator = feeCalculatorFactory.getFeeCalculator(accountType);
            if (feeCalculator != null) {
                Double fee = feeCalculator.calculateFee() * account.getBalance();
                return ResponseEntity.ok(fee);
            } else {
                throw new Exception("Fee calculator not found for account type: " + accountType);
            }
        } else {
            throw new Exception("Account not found with ID: " + accountId);
        }
    }
}
