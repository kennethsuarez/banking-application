package com.project.bankingapplication.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.project.bankingapplication.domain.Account;
import com.project.bankingapplication.domain.Transaction;
import com.project.bankingapplication.exception.AccountNotFoundException;
import com.project.bankingapplication.service.AccountRepository;
import com.project.bankingapplication.service.TransactionRepository;

@RestController
public class AccountController {
	
	private AccountRepository repository;
	
	
	private TransactionRepository transactionRepository;
	
	
	public AccountController(AccountRepository repository, TransactionRepository transactionRepository) {
		this.repository = repository;
		this.transactionRepository = transactionRepository;
	}
	
	
	@GetMapping("/api/accounts/{id}")
	public Optional<Account> retrieveUser(@PathVariable int id) {
		Optional<Account> account = repository.findById(id);
		
		if (account.isEmpty()) 
			throw new AccountNotFoundException("id:"+id);
		
		return account;
	}
	
	
	@PostMapping("/api/accounts")
	public ResponseEntity<Object> createUser(@RequestBody Account account) {
		Account savedAccount = repository.save(account);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedAccount.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/api/accounts/{id}/transactions")
	public List<Transaction> retrieveTransactionsForUser(@PathVariable int id) {
		Optional<Account> account = repository.findById(id);
		
		if (account.isEmpty())
			throw new AccountNotFoundException("id:"+id);
		
		return account.get().getTransactions();
	}
	
	
	@PostMapping("/api/accounts/{id}/transactions")
	public ResponseEntity<Object> createTransaction(@PathVariable int id, @RequestBody Transaction transaction) {
		Optional<Account> account = repository.findById(id);
		Account accountEntity = null;
		
		if (account.isEmpty()) {
			
			throw new AccountNotFoundException("id:"+id);
			
		} else {
			
			accountEntity = account.get();
			
		}
		
		
		transaction.setAccount(account.get());
		
		if (transaction.getTransactionType().equals("deposit")) {
			
			accountEntity.setBalance(accountEntity.getBalance() + transaction.getAmount());
			
		} 
		
		else if (transaction.getTransactionType().equals("withdraw")) {
			
			// amount not sufficient
			if (accountEntity.getBalance() < transaction.getAmount()) {
				
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient Funds.");
				
			} else {
				
				accountEntity.setBalance(accountEntity.getBalance() - transaction.getAmount());
				
			}
		}
		
		Transaction savedTransaction = transactionRepository.save(transaction);
		repository.save(accountEntity);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedTransaction.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
}
