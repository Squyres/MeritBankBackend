package com.meritamerica.capstone.models;

import javax.persistence.Entity;

@Entity
public class RolloverIRA extends IRAAccount {
	static final double DEFAULT_INTEREST_RATE = 0;

	public RolloverIRA() {
		super();
		super.setInterestRate(DEFAULT_INTEREST_RATE);
		super.setMaxAccounts(1);
		setAccountName("Rollover IRA Account");

	}

}