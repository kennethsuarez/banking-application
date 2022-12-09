package com.project.bankingapplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.bankingapplication.domain.Account;
import com.project.bankingapplication.exception.AccountNotFoundException;
import com.project.bankingapplication.repository.AccountRepository;

@Component
public class AccountService {
	
	private AccountRepository accountRepository;
	
	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public Integer register(Account account) {
		Account savedAccount = accountRepository.save(account);
		return savedAccount.getId();
	}
	
	public Optional<Account> getAccount(int id) {
		Optional<Account> account = accountRepository.findById(id);
		
		if (account.isEmpty()) 
			throw new AccountNotFoundException("id:"+id);
		
		return account;
	}
	
	public List<Account> getAllAccounts() {
		return accountRepository.findAll();
	}
	
}
