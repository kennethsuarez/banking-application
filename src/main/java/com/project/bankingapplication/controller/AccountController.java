package com.project.bankingapplication.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
	
	
	@GetMapping("/api/accounts")
	public List<Account> retrieveAllAccounts() {
		return repository.findAll();
	}
	
	
	@GetMapping("/api/accounts/{id}")
	public EntityModel<Optional<Account>> retrieveUser(@PathVariable int id) {
		Optional<Account> account = repository.findById(id);
		
		if (account.isEmpty()) 
			throw new AccountNotFoundException("id:"+id);
		
		EntityModel<Optional<Account>> entityModel = EntityModel.of(account);
		
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllTransactions(id));
		entityModel.add(link.withRel("transactions"));
		
		return entityModel;
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
	public List<Transaction> retrieveAllTransactions(@PathVariable int id) {
		Optional<Account> account = repository.findById(id);
		
		if (account.isEmpty())
			throw new AccountNotFoundException("id:"+id);
		
		return account.get().getTransactions();
	}
	
	

}
