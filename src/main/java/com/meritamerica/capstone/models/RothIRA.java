package com.meritamerica.capstone.models;

import javax.persistence.Entity;

@Entity
public class RothIRA extends IRAAccount {
	static final double DEFAULT_INTEREST_RATE = 0;

	public RothIRA() {
		super();
		super.setInterestRate(DEFAULT_INTEREST_RATE);
		super.setMaxAccounts(1);
		setAccountName("Roth IRA Account");
	}

}