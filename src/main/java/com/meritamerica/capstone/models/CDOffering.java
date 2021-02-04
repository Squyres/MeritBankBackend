
package com.meritamerica.capstone.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class CDOffering {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int term;
	@OneToMany(cascade = CascadeType.ALL)
	private List<CDAccount> cdAccounts;
	private double interestRate;

	public CDOffering(int term, double interestRate) {
		this.term = term;
		this.interestRate = interestRate;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public int getTerm() {
		return term;
	}

	String writeToString() {
		return term + "," + interestRate;
	}
}