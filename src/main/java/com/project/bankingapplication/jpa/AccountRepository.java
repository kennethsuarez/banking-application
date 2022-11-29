package com.project.bankingapplication.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bankingapplication.account.Account;

public interface AccountRepository extends JpaRepository <Account, Integer> {

}
