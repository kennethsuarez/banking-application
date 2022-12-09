package com.project.bankingapplication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.Test;

import com.project.bankingapplication.domain.Account;
import com.project.bankingapplication.exception.AccountNotFoundException;
import com.project.bankingapplication.repository.AccountRepository;
import com.project.bankingapplication.service.AccountService;


public class AccountServiceTest {
	
	private AccountRepository accountRepository = mock(AccountRepository.class);
	
	public AccountService SUT = new AccountService(accountRepository); 

	@Test
	public void Should_ReturAccount_When_AccountExists() {
		Account account = new Account(10001, "Dobrek Zlatokov", 0);
		// given
		given(accountRepository.findById(anyInt())).willReturn(Optional.of(account));
	
		// when
		Optional<Account> result = SUT.getAccount(10001);
		
		// then
		assertThat(result.get().getId()).isEqualTo(account.getId());
		assertThat(result.get().getName()).isEqualTo(account.getName());
		assertThat(result.get().getBalance()).isEqualTo(account.getBalance());
	}
	
	@Test(expected = AccountNotFoundException.class)
	public void Should_ReturNull_When_AccountDoesNotExist() {
		given(accountRepository.findById(10001)).willThrow(AccountNotFoundException.class);
		
		Optional<Account> result = SUT.getAccount(10001);
		
		assertThat(result).isNull();
	}
}
