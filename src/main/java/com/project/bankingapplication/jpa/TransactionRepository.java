package com.project.bankingapplication.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bankingapplication.account.Transaction;

public interface TransactionRepository extends JpaRepository <Transaction, Integer> {

}
