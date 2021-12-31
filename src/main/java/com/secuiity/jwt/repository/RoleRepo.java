package com.secuiity.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secuiity.jwt.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
