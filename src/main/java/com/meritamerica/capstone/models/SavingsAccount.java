package com.meritamerica.capstone.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class SavingsAccount extends BankAccount {

	public static final double INTEREST_RATE = 0.01;

	@ManyToOne
	private AccountHolder ah;
	private int accountHolder;

	public SavingsAccount() {
		this.balance = 0;
		this.interestRate = INTEREST_RATE;
	}

	public SavingsAccount(double openBalance, double interestRate) {
		super(openBalance, interestRate);
	}

	public SavingsAccount(long accountNumber, double openBalance, double interestRate, Date accountOpenedOn) {
		super(accountNumber, openBalance, interestRate, accountOpenedOn);
	}

	public int getAccountHolder() {
		return this.accountHolder;
	}

	public void setAccountHolder(int actId) {
		this.accountHolder = actId;
	}

	@Override
	public String toString() {
		return "Savings Account Balance: $" + balance + "\n" + "Savings Account Interest Rate: " + INTEREST_RATE + "\n"
				+ "Savings Account Balance in 3 years: $" + futureValue(3);

	}
}
