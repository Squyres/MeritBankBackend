package com.meritamerica.capstone.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;

import com.meritamerica.capstone.exceptions.ExceedsFraudSuspicionLimitException;

@Entity
public class CDAccount extends BankAccount {

	@Min(value = 1)
	private int term;
	Date date;

	@ManyToOne
	private AccountHolder ah;
	private int accountHolder;

	@ManyToOne
	private CDOffering offering;

	public CDAccount() {
		this.balance = 0;
		this.interestRate = 0.01;
	}

	public CDAccount(CDOffering offering, double openBalance) {
		super(openBalance, offering.getInterestRate());
		this.offering = offering;
		this.term = offering.getTerm();
	}

	public CDAccount(long accountNumber, double balance, double interestRate, Date accountOpenedOn, int term) {
		super(accountNumber, balance, interestRate, accountOpenedOn);
		this.term = term;
	}

	@Override
	public boolean deposit(double amount) throws ExceedsFraudSuspicionLimitException {
		throw new ExceedsFraudSuspicionLimitException();
	}

	public double futureValue() throws ExceedsFraudSuspicionLimitException {
		throw new ExceedsFraudSuspicionLimitException();
	}

	public int getAccountHolder() {
		return this.accountHolder;
	}

	public Date getStartDate() {
		return date;
	}

	public int getTerm() {
		return this.term;
	}

	public void setAccountHolder(int actId) {
		this.accountHolder = actId;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	@Override
	public boolean withdraw(double amount) throws ExceedsFraudSuspicionLimitException {
		throw new ExceedsFraudSuspicionLimitException();
	}

}
