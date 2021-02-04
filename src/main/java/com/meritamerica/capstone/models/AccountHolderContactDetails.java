package com.meritamerica.capstone.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "account_holder_contact_details")
public class AccountHolderContactDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	@OneToOne(cascade = CascadeType.ALL)
	private AccountHolder accountHolder;

	private String phoneNumber;

	private String email;

	private String address;

	public AccountHolder getAccountHolder() {
		return accountHolder;
	}

	public String getAddress() {
		return address;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setAccountHolder(AccountHolder accountHolder) {
		this.accountHolder = accountHolder;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
