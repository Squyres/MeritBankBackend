package com.meritamerica.capstone.models;

import java.util.Arrays;

class MeritBank {

	private static long nextAccountNumber;
	private static AccountHolder AccountHoldersArray[] = new AccountHolder[0];
	private static CDOffering CDOfferingsArray[] = new CDOffering[0];

	public static void addAccountHolder(AccountHolder accountHolder) {
		AccountHolder[] newAccountHolderArray = new AccountHolder[AccountHoldersArray.length + 1];
		for (int i = 0; i < newAccountHolderArray.length - 1; i++) {
			newAccountHolderArray[i] = AccountHoldersArray[i];
		}
		AccountHoldersArray = newAccountHolderArray;
		AccountHoldersArray[AccountHoldersArray.length - 1] = accountHolder;
	}

	public static void clearCDOfferings() {
		CDOfferingsArray = null;
	}

	public static double futureValue(double presentValue, double interestRate, int term) {
		interestRate += 1;
		return presentValue * recursiveFutureValue(presentValue, term, interestRate);
	}

	public static AccountHolder[] getAccountHolders() {
		return AccountHoldersArray;
	}

	public static CDOffering getBestCDOffering(double depositAmount) {
		double best = 0.0;
		CDOffering bestOffering = null;
		if (CDOfferingsArray == null) {
			return null;
		}
		for (CDOffering offering : CDOfferingsArray) {
			if (futureValue(depositAmount, offering.getInterestRate(), offering.getTerm()) > best) {
				bestOffering = offering;
				best = futureValue(depositAmount, offering.getInterestRate(), offering.getTerm());
			}
		}
		return bestOffering;
	}

	public static CDOffering[] getCDOfferings() {
		return CDOfferingsArray;
	}

	public static long getNextAccountNumber() {
		return nextAccountNumber++;
	}

	public static double recursiveFutureValue(double amount, int years, double interestRate) {
		if (years != 0) {
			return (interestRate * recursiveFutureValue(amount, years - 1, interestRate));
		} else
			return 1;
	}

	public static void setCDOfferings(CDOffering[] offerings) {
		CDOfferingsArray = offerings;
	}

	static void setNextAccountNumber(long accountNumber) {
		nextAccountNumber = accountNumber;

	}

	static AccountHolder[] sortAccountHolders() {
		Arrays.sort(AccountHoldersArray);
		for (AccountHolder a : AccountHoldersArray) {
			System.out.println(a);
		}
		return AccountHoldersArray;
	}

	public static double totalBalances() {
		double total = 0.0;
		for (AccountHolder accounts : AccountHoldersArray) {
			total += accounts.getCombinedBalance();
		}
		System.out.println("Total aggregate account balance: $" + total);
		return total;

	}
}