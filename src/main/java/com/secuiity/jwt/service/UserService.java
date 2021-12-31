package com.secuiity.jwt.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.secuiity.jwt.entity.AppUser;
import com.secuiity.jwt.entity.Role;

public interface UserService extends UserDetailsService {
	public AppUser save(AppUser appUser);

	public Role save(Role role);

	public void addRoleToAppUser(String username, String role);

	public AppUser getUser(String username);

	public List<AppUser> getUsers();
}
