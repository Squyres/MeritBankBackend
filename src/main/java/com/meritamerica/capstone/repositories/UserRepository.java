package com.meritamerica.capstone.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.capstone.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findById(int id);

	Optional<User> findByUserName(String userName);

}