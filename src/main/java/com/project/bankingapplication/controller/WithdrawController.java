package com.project.bankingapplication.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
import com.project.bankingapplication.repository.AccountRepository;
import com.project.bankingapplication.repository.TransactionRepository;

@RestController
public class WithdrawController {
	
	private AccountRepository repository;
	
	private TransactionRepository transactionRepository;
	
	
	public WithdrawController(AccountRepository repository, TransactionRepository transactionRepository) {
		this.repository = repository;
		this.transactionRepository = transactionRepository;
	}
	
	
	@PostMapping("/api/accounts/{id}/withdraw")
	public ResponseEntity<Object> withdrawMoney(@PathVariable int id, @RequestBody Transaction transaction) {
		Optional<Account> account = repository.findById(id);
		Account accountEntity = null;
		
		if (account.isEmpty()) {
			
			throw new AccountNotFoundException("id:"+id);
			
		} else {
			
			accountEntity = account.get();
			
		}
		
		transaction.setAccount(accountEntity);
		
			
		// amount not sufficient
		if (accountEntity.getBalance() < transaction.getAmount()) {
				
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient Funds.");
				
		} else {
				
			accountEntity.setBalance(accountEntity.getBalance() - transaction.getAmount());
				
		}
		
		transaction.setTransactionType("withdraw");
		Transaction savedTransaction = transactionRepository.save(transaction);
		repository.save(accountEntity);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedTransaction.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
}
