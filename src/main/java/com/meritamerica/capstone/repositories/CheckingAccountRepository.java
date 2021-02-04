package com.meritamerica.capstone.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.capstone.models.CheckingAccount;

public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Integer> {

	List<CheckingAccount> findByAccountHolder(int id);

}
