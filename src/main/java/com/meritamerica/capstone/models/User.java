package com.meritamerica.capstone.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "User")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String userName;

	private String password;

	private boolean active;

	private String roles;

	@OneToOne(cascade = CascadeType.ALL)
	@JsonIgnore
	@JoinColumn(name = "account_id")
	private AccountHolder accountHolder;

	public User() {
		userName = "";
		password = "";
		active = true;
		roles = "";
	}

	public AccountHolder getAccountHolder() {
		return accountHolder;
	}

	public int getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public String getRoles() {
		return roles;
	}

	public String getUserName() {
		return userName;
	}

	public boolean isActive() {
		return active;
	}

	public void setAccountHolder(AccountHolder accountHolder) {
		this.accountHolder = accountHolder;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}