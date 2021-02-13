package com.meritamerica.capstone.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

import com.meritamerica.capstone.exceptions.CannotCloseAccountException;
import com.meritamerica.capstone.exceptions.ExceedsAvailableBalanceException;
import com.meritamerica.capstone.exceptions.NegativeAmountException;
import com.meritamerica.capstone.exceptions.TransactionNotAllowedException;

@Entity
public abstract class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int accountNumber;
	@Min(value = 0)
	private double balance;
	@DecimalMin(value = "0.0", inclusive = true, message = "Interest rate must be > 0")
	@DecimalMax(value = "1.0", inclusive = true, message = "Interest rate must be < 1")
	private double interestRate;
	Date accountOpenedOn;
	private int term;
	private int userId;
	private int maxAccounts;
	@OneToMany(cascade = CascadeType.ALL)
	protected List<Transaction> transactions;
	private boolean active;
	private String accountName;

	public BankAccount() {
		this.maxAccounts = 0;
		this.balance = 0;
		this.accountOpenedOn = new Date();
		transactions = new ArrayList<Transaction>();
		this.active = true;
		this.term = -1;
	}

	public void addTransaction(Transaction t) {
		this.transactions.add(t);
	}

	public CDOffering cdOfferings() {
		return cdOfferings();
	}

	public Transaction closeAccount(AccountHolder user) throws TransactionNotAllowedException,
			ExceedsAvailableBalanceException, NegativeAmountException, CannotCloseAccountException {

		BankAccount targetAccount = user.getSingleSavingsAccount();
		Transaction t = null;
		double amount = this.balance;

		this.active = false;
		try {
			withdraw(amount);
			targetAccount.deposit(amount);

			t = new Transaction();
			t.setAmount(amount);
			t.setTargetAccount(targetAccount.getAccountNumber());
			t.setSourceAccount(targetAccount.getAccountNumber());
			t.setTransactionSuccess(true);
			t.setTransactionMemo("Closed Account #" + this.accountNumber);
			t.setBalanceAfterTransaction(targetAccount.getBalance());

			List<Transaction> lt = targetAccount.getTransactions();
			lt.add(t);
			targetAccount.setTransactions(lt);

		} catch (Exception expected) {
		}

		return t;
	}

	public void deposit(double amount)
			throws TransactionNotAllowedException, ExceedsAvailableBalanceException, NegativeAmountException {

		if (amount < 0) {
			throw new NegativeAmountException("Unable to process");
		}
		this.balance += amount;
	}

	public double futureValue(int years) {
		if (years == 0) {
			return this.balance;
		}
		return futureValue(years - 1) * (1 + this.interestRate);
	}

	public String getAccountName() {
		return accountName;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public Date getAccountOpenedOn() {
		return accountOpenedOn;
	}

	public double getBalance() {
		return balance;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public int getMaxAccounts() {
		return maxAccounts;
	}

	public int getTerm() {
		return term;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public long getUserId() {
		return userId;
	}

	public boolean isActive() {
		return active;
	}

	protected Transaction multipleAccountTransaction(Transaction t, BankAccount source, BankAccount target) {
		boolean madeWithdraw = false;

		try {
			source.withdraw(t.getAmount());
			madeWithdraw = true;
			target.deposit(t.getAmount());
			t.setTransactionSuccess(true);
		} catch (Exception e) {
			if (madeWithdraw) {
				source.setBalance(source.getBalance() + t.getAmount());
			}
			t.setTransactionSuccess(false);

		}

		return t;
	}

	public Transaction processTransaction(Transaction t, BankAccount source, BankAccount target) {
		if (source.equals(target)) {
			t = singleAccountTransaction(t);
		} else {
			t = multipleAccountTransaction(t, source, target);
		}

		t.setBalanceAfterTransaction(this.getBalance());
		this.transactions.add(t);

		return t;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setAccountOpenedOn(Date accountOpenedOn) {
		this.accountOpenedOn = accountOpenedOn;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public void setMaxAccounts(int maxAccounts) {
		this.maxAccounts = maxAccounts;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	protected Transaction singleAccountTransaction(Transaction t) {
		try {
			if (t.getAmount() > 0) {
				this.deposit(t.getAmount());
			}
			if (t.getAmount() < 0) {
				this.withdraw(-1 * t.getAmount());
			}

			t.setTransactionSuccess(true);

		} catch (Exception e) {
			t.setTransactionSuccess(false);
		}

		return t;
	}

	public void withdraw(double amount)
			throws TransactionNotAllowedException, ExceedsAvailableBalanceException, NegativeAmountException {
		if (amount > this.balance) {
			throw new ExceedsAvailableBalanceException("Exceeds Available Balance");
		}
		if (amount < 0) {
			throw new NegativeAmountException("Unable to process");
		}
		this.balance -= amount;
	}

}
