package com.meritamerica.capstone.models;

import javax.persistence.Entity;

import com.meritamerica.capstone.exceptions.CannotCloseAccountException;
import com.meritamerica.capstone.exceptions.ExceedsAvailableBalanceException;
import com.meritamerica.capstone.exceptions.NegativeAmountException;

@Entity
public class SavingsAccount extends BankAccount {

	static final double DEFAULT_INTEREST_RATE = .01;

	public SavingsAccount() {
		super();
		super.setInterestRate(DEFAULT_INTEREST_RATE);
		super.setMaxAccounts(1);
		setAccountName("Savings Account");
	}

	@Override
	public Transaction closeAccount(AccountHolder user)
			throws ExceedsAvailableBalanceException, NegativeAmountException, CannotCloseAccountException {
		throw new CannotCloseAccountException();
	}
}