package com.secuiity.jwt.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.secuiity.jwt.entity.AppUser;
import com.secuiity.jwt.entity.Role;
import com.secuiity.jwt.repository.RoleRepo;
import com.secuiity.jwt.repository.UserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
	private final UserRepo userRepo;
	private final RoleRepo roleRepo;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = this.userRepo.findByUsername(username);
		if (appUser == null) {
			log.error("User Not found in DB");
			throw new UsernameNotFoundException("User Not found in DB");
		} else {
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			appUser.getRole().stream().forEach(x -> {
				authorities.add(new SimpleGrantedAuthority(x.getName()));
			});
			return new User(appUser.getUsername(), appUser.getPassword(), authorities);
		}
	}

	@Override
	public AppUser save(AppUser appUser) {
		// TODO Auto-generated method stub
		log.info("Saving user {} into the DB ", appUser.getUsername());
		appUser.setPassword(this.passwordEncoder.encode(appUser.getPassword()));
		return this.userRepo.save(appUser);
	}

	@Override
	public Role save(Role role) {
		// TODO Auto-generated method stub
		log.info("Saving role {} into the DB ", role.getName());
		return this.roleRepo.save(role);
	}

	@Override
	public void addRoleToAppUser(String username, String roleName) {
		// TODO Auto-generated method stub
		AppUser appUser = this.userRepo.findByUsername(username);
		Role role = this.roleRepo.findByName(roleName);

		log.info("Saving role {} to user {} ", roleName, username);
		appUser.getRole().add(role);
	}

	@Override
	public AppUser getUser(String username) {
		// TODO Auto-generated method stub
		return this.userRepo.findByUsername(username);
	}

	@Override
	public List<AppUser> getUsers() {
		// TODO Auto-generated method stub
		return this.userRepo.findTop10By();
	}

}
