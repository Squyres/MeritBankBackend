
package com.meritamerica.capstone.models;

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

@Entity
public class CDOffering {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Min(1)
	private int term;
	@OneToMany(cascade = CascadeType.ALL)
	private List<CDAccount> cdAccounts;
	@DecimalMin("0.0")
	@DecimalMax("1.0")
	private double interestRate;

	public CDOffering() {
	}

	public CDOffering(int term, double interestRate) {
		this.term = term;
		this.interestRate = interestRate;
	}

	public long getId() {
		return this.id;
	}

	public double getInterestRate() {
		return this.interestRate;
	}

	public int getTerm() {
		return this.term;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setInterestRate(double n) {
		this.interestRate = n;
	}

	public void setTerm(int n) {
		this.term = n;
	}
}