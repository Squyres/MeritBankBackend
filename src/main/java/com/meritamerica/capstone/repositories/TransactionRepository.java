package com.meritamerica.capstone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meritamerica.capstone.models.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	Transaction findById(int id);

}