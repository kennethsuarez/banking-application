package com.project.bankingapplication.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.project.bankingapplication.service.AccountService;


@RestController
public class AccountController {
	
	
	@Autowired
	private AccountService accountService;
	
//	@Autowired
//	public AccountController(AccountRepository repository) {
//		this.repository = repository;
//	}
	
	
	@GetMapping("/api/accounts")
	public List<Account> retrieveAllAccounts() {
		return accountService.getAllAccounts();
	}
	
	
	@GetMapping("/api/accounts/{id}")
	public EntityModel<Optional<Account>> retrieveAccount(@PathVariable int id) {
		
		Optional<Account> account = accountService.getAccount(id);
		
		EntityModel<Optional<Account>> entityModel = EntityModel.of(account);
		
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllTransactions(id));
		entityModel.add(link.withRel("transactions"));
		
		return entityModel;
	}
	
	
	@PostMapping("/api/accounts")
	public ResponseEntity<Object> createAccount(@RequestBody Account account) {
		Integer id = accountService.register(account);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(id)
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/api/accounts/{id}/transactions")
	public List<Transaction> retrieveAllTransactions(@PathVariable int id) {
		Optional<Account> account = accountService.getAccount(id);
		
		return account.get().getTransactions();
	}
	
	

}
