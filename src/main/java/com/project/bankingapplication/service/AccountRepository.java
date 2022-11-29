package com.project.bankingapplication.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bankingapplication.domain.Account;

public interface AccountRepository extends JpaRepository <Account, Integer> {

}
