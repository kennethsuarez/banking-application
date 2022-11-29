package com.project.bankingapplication.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Transaction {
	@Id 
	@GeneratedValue
	private Integer id;
	private String transactionType;
	private double amount;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Account account;


	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getTransactionType() {
		return transactionType;
	}



	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}



	public double getAmount() {
		return amount;
	}



	public void setAmount(double amount) {
		this.amount = amount;
	}



	public Account getAccount() {
		return account;
	}



	public void setAccount(Account account) {
		this.account = account;
	}



	@Override
	public String toString() {
		return "Transaction [id=" + id + ", transactionType=" + transactionType + ", amount=" + amount + "]";
	}
	
	

	
	
}
