package com.example.movieservice.controllers;

import com.example.movieservice.entities.AccountHolder;
import com.example.movieservice.repository.AccountHolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class AccountHolderController {

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @GetMapping("/getUsers")
    public List<AccountHolder> getAllAccountHolders() {
        return accountHolderRepository.findAll();
    }

    // API to create a new account holder
    @PostMapping("/createUsers")
    public AccountHolder createAccountHolder(@RequestBody AccountHolder accountHolder) {
        return accountHolderRepository.save(accountHolder);
    }

    @GetMapping("/user/{id}")
    public AccountHolder getAccountHolderById(@PathVariable Long id) {
        return accountHolderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account holder not found"));
    }

    @PutMapping("/updateUser/{id}")
    public AccountHolder updateAccountHolder(@PathVariable Long id, @RequestBody AccountHolder accountHolderDetails) {
        AccountHolder accountHolder = accountHolderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account holder not found"));

        accountHolder.setUsername(accountHolderDetails.getUsername());

        return accountHolderRepository.save(accountHolder);
    }

    @DeleteMapping("/deleteUser/{id}")
    public void deleteAccountHolder(@PathVariable Long id) {
        accountHolderRepository.deleteById(id);
    }
}
