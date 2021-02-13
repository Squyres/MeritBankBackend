package com.meritamerica.capstone.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meritamerica.capstone.exceptions.MaxAccountsReachedException;

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
	@OneToMany(cascade = CascadeType.ALL)
	private List<BankAccount> bankAccounts;
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
	private String closedAccounts;

	public AccountHolder() {
		bankAccounts = new ArrayList<>();
		this.active = true;
	}

	public BankAccount addBankAccount(BankAccount bankAccount) throws MaxAccountsReachedException {

		if (bankAccount.getBalance() < 0) {
			System.out.println("Can't Deposit Negative Amount");
		}
		if (bankAccount.getMaxAccounts() > 0
				&& getNumberOfAccountsByType(bankAccount) >= bankAccount.getMaxAccounts()) {
			throw new MaxAccountsReachedException();
		}

		bankAccounts.add(bankAccount);
		return bankAccount;
	}

	public String getAddress() {
		return address;
	}

	public double getAllAvailableBalance() {
		double sum = 0;

		sum += getBalanceByType(new CheckingAccount());
		sum += getBalanceByType(new SavingsAccount());
		sum += getBalanceByType(new DBACheckingAccount());

		double penSum = getBalanceByType(new RegularIRA());
		penSum += getBalanceByType(new RothIRA());
		penSum += getBalanceByType(new RolloverIRA());

		penSum /= 1.2;
		penSum = Math.floor(penSum * 100);
		penSum /= 100;

		return (sum + penSum);
	}

	public String getAuthority() {
		return authority;
	}

	public double getBalanceByType(BankAccount type) {
		double sum = 0;
		for (BankAccount b : bankAccounts) {
			if (b.getClass() == type.getClass() && b.isActive()) {
				sum += b.getBalance();
			}
		}
		return sum;
	}

	public List<BankAccount> getBankAccounts() {
		List<BankAccount> ba = new ArrayList<BankAccount>();
		for (BankAccount b : this.bankAccounts) {
			if (b.isActive()) {
				ba.add(b);
			}
		}
		return ba;
	}

	public List<BankAccount> getCDAccount() {
		List<BankAccount> accounts = new ArrayList<>();
		for (BankAccount b : this.bankAccounts) {
			if (b instanceof CDAccount && b.isActive()) {
				accounts.add(b);
			}
		}
		return accounts;
	}

	public List<BankAccount> getCheckingAccounts() {
		List<BankAccount> accounts = new ArrayList<>();
		for (BankAccount b : this.bankAccounts) {
			if (b instanceof CheckingAccount && b.isActive()) {
				accounts.add(b);
			}
		}
		return accounts;
	}

	public String getCity() {
		return city;
	}

	public String getClosedAccounts() {
		if (closedAccounts == null) {
			return "";
		}
		return closedAccounts;
	}

	public double getCombinedBalance() {

		double sum = 0;
		sum += getBalanceByType(new CheckingAccount());
		sum += getBalanceByType(new SavingsAccount());
		sum += getBalanceByType(new CDAccount());
		sum += getBalanceByType(new DBACheckingAccount());
		sum += getBalanceByType(new RegularIRA());
		sum += getBalanceByType(new RothIRA());
		sum += getBalanceByType(new RolloverIRA());
		return sum;
	}

	public List<BankAccount> getDBACheckingAccounts() {
		List<BankAccount> accounts = new ArrayList<>();
		for (BankAccount b : this.bankAccounts) {
			if (b instanceof DBACheckingAccount && b.isActive()) {
				accounts.add(b);
			}
		}
		return accounts;
	}

	public List<BankAccount> getRothIRA() {
		List<BankAccount> accounts = new ArrayList<>();
		for (BankAccount b : this.bankAccounts) {
			if (b instanceof RothIRA && b.isActive()) {
				accounts.add(b);
			}
		}
		return accounts;
	}

	public List<BankAccount> getRolloverIRA() {
		List<BankAccount> accounts = new ArrayList<>();
		for (BankAccount b : this.bankAccounts) {
			if (b instanceof RolloverIRA && b.isActive()) {
				accounts.add(b);
			}
		}
		return accounts;
	}

	public List<BankAccount> getRegularIRA() {
		List<BankAccount> accounts = new ArrayList<>();
		for (BankAccount b : this.bankAccounts) {
			if (b instanceof RegularIRA && b.isActive()) {
				accounts.add(b);
			}
		}
		return accounts;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public int getId() {
		return id;
	}

	public List<BankAccount> getInactiveBankAccounts() {
		List<BankAccount> ba = new ArrayList<BankAccount>();
		for (BankAccount b : this.bankAccounts) {
			if (!b.isActive()) {
				ba.add(b);
			}
		}
		return ba;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public int getNumberOfAccountsByType(BankAccount type) {
		int sum = 0;
		for (BankAccount b : this.bankAccounts) {
			if (b.getClass() == type.getClass() && b.isActive()) {
				sum++;
			}
		}
		return sum;
	}

	public String getPassword() {
		return password;
	}

	public String getPhone() {
		return phone;
	}

	public List<BankAccount> getSavingsAccount() {
		List<BankAccount> accounts = new ArrayList<>();
		for (BankAccount b : this.bankAccounts) {
			if (b instanceof SavingsAccount && b.isActive()) {
				accounts.add(b);
			}
		}
		return accounts;
	}

	@JsonIgnore
	public BankAccount getSingleSavingsAccount() {
		return getSavingsAccount().get(0);
	}

	public String getSsn() {
		return ssn;
	}

	public String getState() {
		return state;
	}

	public String getUsername() {
		return username;
	}

	public double getValue() {
		return value;
	}

	public String getZip() {
		return zip;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean isActive) {
		this.active = isActive;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public void setBankAccounts(List<BankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setClosedAccounts(String closedAccounts) {
		this.closedAccounts = closedAccounts;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public void updateContactInfo(AccountHolder temp) {
		this.address = temp.getAddress();
		this.city = temp.getCity();
		this.zip = temp.getZip();
		this.phone = temp.getPhone();
		this.email = temp.getEmail();
	}

}