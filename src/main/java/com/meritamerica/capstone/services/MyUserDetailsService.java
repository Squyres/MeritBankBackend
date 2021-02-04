package com.meritamerica.capstone.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.meritamerica.capstone.models.AccountHolder;
import com.meritamerica.capstone.repositories.AccountHolderRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	AccountHolderRepository accountHolderRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		AccountHolder accountHolder = accountHolderRepository.findByUsername(username);

		if (accountHolder != null) {
			if (!accountHolder.isActive()) {
				return null;
			}
			String userLevel = accountHolder.getAuthority();
			Set<SimpleGrantedAuthority> authorities = new HashSet<>();
			authorities.add(new SimpleGrantedAuthority(userLevel));

			return new User(username, accountHolder.getPassword(), authorities);
		}

		return null;
	}
}