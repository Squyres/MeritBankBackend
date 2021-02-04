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
public class AccountHolder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ah")
	List<CDAccount> cdAccountArray;
	private String username;
	private String password;
	private String authority;
	private String email;
	private String phone;
	private String address;
	private String city;
	private String state;
	private String zip;
	private double value;
	private boolean active;

	public AccountHolder() {
		checkingArray = new ArrayList<CheckingAccount>();
		savingsArray = new ArrayList<SavingsAccount>();
		cdAccountArray = new ArrayList<CDAccount>();
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
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

	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

}
