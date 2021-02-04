package com.meritamerica.capstone.models;

import javax.persistence.Entity;

import com.meritamerica.capstone.exceptions.TransactionNotAllowedException;

@Entity
public class CDAccount extends BankAccount {

	public CDAccount() {
		super();
		setAccountName("CD Account");
	}

	@Override
	public void deposit(double amount) throws TransactionNotAllowedException {
		throw new TransactionNotAllowedException();
	}

	@Override
	public void withdraw(double amount) throws TransactionNotAllowedException {
		throw new TransactionNotAllowedException();
	}

}