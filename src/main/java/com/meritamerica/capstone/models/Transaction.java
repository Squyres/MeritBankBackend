package com.meritamerica.capstone.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private int sourceAccount;
	private int targetAccount;

	private Date transactionDate;
	private double amount;
	private String transactionMemo;
	private boolean transactionSuccess;

	private double balanceAfterTransaction;

	public Transaction() {
		this.transactionDate = new Date();
		this.amount = 0;
		this.transactionMemo = "";
	}

	public double getAmount() {
		return amount;
	}

	public double getBalanceAfterTransaction() {
		return balanceAfterTransaction;
	}

	public long getId() {
		return id;
	}

	public int getSourceAccount() {
		return sourceAccount;
	}

	public int getTargetAccount() {
		return targetAccount;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public String getTransactionMemo() {
		return transactionMemo;
	}

	public boolean getTransactionSuccess() {
		return this.transactionSuccess;
	}

	public boolean isTransactionSuccess() {
		return transactionSuccess;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setBalanceAfterTransaction(double balanceAfterTransaction) {
		this.balanceAfterTransaction = balanceAfterTransaction;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSourceAccount(int sourceAccount) {
		this.sourceAccount = sourceAccount;
	}

	public void setTargetAccount(int targeAccount) {
		this.targetAccount = targeAccount;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public void setTransactionMemo(String transactionMemo) {
		this.transactionMemo = transactionMemo;
	}

	public void setTransactionSuccess(boolean transactionSuccess) {
		this.transactionSuccess = transactionSuccess;
	}

}