package com.meritamerica.capstone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.capstone.models.CDOffering;

public interface CDOfferingRepository extends JpaRepository<CDOffering, Integer> {

	CDOffering findById(int id);

}
