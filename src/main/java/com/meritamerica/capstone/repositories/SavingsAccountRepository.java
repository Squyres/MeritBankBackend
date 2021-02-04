package com.meritamerica.capstone.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.capstone.models.SavingsAccount;

public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Integer> {
	List<SavingsAccount> findByAccountHolder(int id);

}
