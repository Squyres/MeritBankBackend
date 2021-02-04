package com.meritamerica.capstone.models;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

import com.meritamerica.capstone.exceptions.ExceedsFraudSuspicionLimitException;

@MappedSuperclass
@Table(name = "bankAccounts")
public abstract class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int accountNumber;
	@Min(value = 0)
	double balance;
	@DecimalMin(value = "0.0", inclusive = false, message = "Interest rate must be > 0")
	@DecimalMax(value = "1.0", inclusive = false, message = "Interest rate must be < 1")
	double interestRate;
	Date accountOpenedOn;

	public BankAccount() {
		this.balance = 0;
		this.interestRate = 0;
	}

	public BankAccount(double balance, double interestRate) {
		this.balance = balance;
		this.interestRate = interestRate;
		this.accountOpenedOn = new Date();
	}

	BankAccount(double balance, double interestRate, Date accountOpenedOn) {
		this.balance = balance;
		this.interestRate = interestRate;
		this.accountOpenedOn = accountOpenedOn;
	}

	public BankAccount(long accountNumber, double balance, double interestRate, Date accountOpenedOn) {
		this.balance = balance;
		this.interestRate = interestRate;
		this.accountOpenedOn = accountOpenedOn;
	}

	public boolean deposit(double amount) throws ExceedsFraudSuspicionLimitException {
		if (amount <= 0) {
			System.out.println("Please deposit sufficient amount");
			return false;
		} else {
			balance += amount;
			System.out.println("Transation Complete");
			return true;
		}
	}

	public double futureValue(int years) {
		double futureVal = this.balance * Math.pow(1 + getInterestRate(), years);
		return futureVal;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public double getBalance() {
		return balance;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public java.util.Date getOpenedOn() {
		return accountOpenedOn;
	}

	public boolean withdraw(double amount) throws ExceedsFraudSuspicionLimitException {
		if (amount <= balance && amount > 0) {
			this.balance -= amount;
			System.out.println("Withdrawn amount: $" + amount);
			System.out.println("Remaining balance: $" + balance);
			return true;
		}
		return false;
	}

}
