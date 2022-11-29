package com.project.bankingapplication.account;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.project.bankingapplication.exception.AccountNotFoundException;
import com.project.bankingapplication.jpa.AccountRepository;
import com.project.bankingapplication.jpa.TransactionRepository;

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
		
		if (account.isEmpty())
			throw new AccountNotFoundException("id:"+id);
		
		transaction.setAccount(account.get());
		
		if (transaction.getTransactionType() == "deposit") {
			account.
		} else if (transaction.getTransactionType() == "withdraw") {
			
		}
		
		Transaction savedTransaction = transactionRepository.save(transaction);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedTransaction.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
}
