package com.meritamerica.capstone.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.capstone.models.AccountHolder;

public interface AccountHolderRepository extends JpaRepository<AccountHolder, Integer> {
	AccountHolder findByFirstName(String fName);

	AccountHolder findById(int id);

	AccountHolder findByUsername(String username);
}
