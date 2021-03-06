package com.meritamerica.capstone.models;

import javax.persistence.Entity;

@Entity
public class DBACheckingAccount extends BankAccount {

	static final double DEFAULT_INTEREST_RATE = .0002;

	public DBACheckingAccount() {
		super();
		super.setInterestRate(DEFAULT_INTEREST_RATE);
		super.setMaxAccounts(3);
		setAccountName("DBA Checking Account");
	}
}