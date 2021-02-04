package com.meritamerica.capstone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.capstone.models.AccountHolder;

public interface AccountHolderRepository extends JpaRepository<AccountHolder, Integer> {
	AccountHolder findByFirstName(String fName);

	AccountHolder findById(int id);
}
