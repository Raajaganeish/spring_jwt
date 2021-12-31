package com.secuiity.jwt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secuiity.jwt.entity.AppUser;

public interface UserRepo extends JpaRepository<AppUser, Long> {
	AppUser findByUsername(String username);

	List<AppUser> findFirst10ByUsername(String username);

	List<AppUser> findTop10By();
}
