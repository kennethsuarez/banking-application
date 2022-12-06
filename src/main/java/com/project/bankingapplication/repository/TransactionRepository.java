package com.project.bankingapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bankingapplication.domain.Transaction;

public interface TransactionRepository extends JpaRepository <Transaction, Integer> {

}
