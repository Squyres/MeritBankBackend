package com.meritamerica.capstone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.capstone.models.AccountHolderContactDetails;

public interface AccountHolderContactDetailsRepository extends JpaRepository<AccountHolderContactDetails, Integer> {
	AccountHolderContactDetails findByAccountHolder(int id);
}
