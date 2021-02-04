package com.meritamerica.capstone.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.capstone.models.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

	BankAccount findById(int id);

	List<BankAccount> findByUserId(int id);

}