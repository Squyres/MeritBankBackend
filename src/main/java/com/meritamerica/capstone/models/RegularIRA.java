package com.meritamerica.capstone.models;

import javax.persistence.Entity;

@Entity
public class RegularIRA extends IRAAccount {
	static final double DEFAULT_INTEREST_RATE = 0;

	public RegularIRA() {
		super();
		super.setInterestRate(DEFAULT_INTEREST_RATE);
		super.setMaxAccounts(1);
		setAccountName("IRA Account");
	}

}