package com.meritamerica.capstone.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "accountholders")
public class AccountHolder implements Comparable<AccountHolder> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
	@Column(name = "user_id")
	private int id;
	@NotBlank
	private String firstName;
	private String middleName;
	@NotBlank
	private String lastName;
	@NotBlank
	private String ssn;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ah")
	List<CheckingAccount> checkingArray;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ah")
	List<SavingsAccount> savingsArray;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private AccountHolderContactDetails accountHolderContactDetails;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ah")
	List<CDAccount> cdAccountArray;

	public AccountHolder() {
		checkingArray = new ArrayList<CheckingAccount>();
		savingsArray = new ArrayList<SavingsAccount>();
		cdAccountArray = new ArrayList<CDAccount>();
	}

	public boolean addCDAccount(CDAccount cdAccount) {
		if (cdAccount == null) {
			return false;
		}
		cdAccountArray.add(cdAccount);
		cdAccount.setAccountHolder(this.id);
		return true;
	}

	public boolean addCheckingAccount(CheckingAccount checkingAccount) {
		if (checkingAccount == null) {
			return false;
		}
		checkingArray.add(checkingAccount);
		checkingAccount.setAccountHolder(this.id);
		return true;
	}

	public boolean addSavingsAccount(SavingsAccount savingsAccount) {
		if (savingsAccount == null) {
			return false;
		}
		savingsArray.add(savingsAccount);
		savingsAccount.setAccountHolder(this.id);
		return true;
	}

	@Override
	public int compareTo(AccountHolder account) {
		if (this.getCombinedBalance() > account.getCombinedBalance()) {
			return 1;
		} else {
			return -1;
		}
	}

	public AccountHolderContactDetails getAccountHolderContactDetails() {
		return accountHolderContactDetails;
	}

	public List<CDAccount> getCDAccounts() {
		return cdAccountArray;
	}

	public double getCDBalance() {
		double total = 0.0;
		for (CDAccount balance : cdAccountArray) {
			total += balance.getBalance();
		}
		return total;
	}

	public List<CheckingAccount> getCheckingAccounts() {
		return checkingArray;
	}

	public double getCheckingBalance() {
		double total = 0.0;
		for (CheckingAccount balance : checkingArray) {
			total += balance.getBalance();
		}
		return total;
	}

	public double getCombinedBalance() {
		return getCDBalance() + getSavingsBalance() + getCheckingBalance();
	}

	public String getFirstName() {
		return firstName;
	}

	public int getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public int getNumberOfCDAccounts() {
		return cdAccountArray.size();
	}

	public int getNumberOfCheckingAccounts() {
		return checkingArray.size();
	}

	public int getNumberOfSavingsAccounts() {
		return savingsArray.size();
	}

	public List<SavingsAccount> getSavingsAccounts() {
		return savingsArray;
	}

	public double getSavingsBalance() {
		double total = 0.0;
		for (SavingsAccount balance : savingsArray) {
			total += balance.getBalance();
		}
		return total;

	}

	public String getSSN() {
		return ssn;
	}

	public void setAccountHolderContactDetails(AccountHolderContactDetails accountHolderContactDetails) {
		this.accountHolderContactDetails = accountHolderContactDetails;
	}

	public void setFirstName(String first) {
		this.firstName = first;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLastName(String last) {
		this.lastName = last;
	}

	public void setMiddleName(String middle) {
		this.middleName = middle;
	}

	public void setSSN(String ssn) {
		this.ssn = ssn;
	}

	@Override
	public String toString() {
		return "Combined Balance for Account Holder" + this.getCombinedBalance();
	}

}
