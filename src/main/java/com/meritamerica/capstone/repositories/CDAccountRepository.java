package com.meritamerica.capstone.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.capstone.models.CDAccount;

public interface CDAccountRepository extends JpaRepository<CDAccount, Integer> {
	List<CDAccount> findByAccountHolder(int id);

}
