package com.secuiity.jwt.controller;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.secuiity.jwt.entity.AppUser;
import com.secuiity.jwt.entity.Role;
import com.secuiity.jwt.service.UserService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final UserService userService;

	@GetMapping(value = "/users")
	public ResponseEntity<List<?>> getAllUsers(HttpServletRequest request) {
		log.info("Request Data {} ", request.getServletPath());
		System.out.println(request.getServletPath());
		return ResponseEntity.ok().body(this.userService.getUsers());
	}

	@PostMapping(value = "/save/user")
	public ResponseEntity<AppUser> saveUser(@RequestBody AppUser appUser, HttpServletRequest request) {
		log.info("Request Data {} ", request.getServletPath());
		System.out.println(request.getServletPath());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/save/user").toUriString());
		return ResponseEntity.created(uri).body(this.userService.save(appUser));
	}

	@PostMapping(value = "/save/role")
	public ResponseEntity<Role> saveRole(@RequestBody Role role, HttpServletRequest request) {
		log.info("Request Data {} ", request.getServletPath());
		System.out.println(request.getServletPath());

		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(request.getServletPath()).toUriString());
		return ResponseEntity.created(uri).body(this.userService.save(role));
	}

	@PostMapping(value = "/add/addRoleToUser")
	public ResponseEntity<Role> addRoleToUser(@RequestBody RoleToUser roleToUser, HttpServletRequest request) {
		this.userService.addRoleToAppUser(roleToUser.getUserName(), roleToUser.getRoleName());
		return ResponseEntity.ok().build();
	}

}

@Data
class RoleToUser {
	private String userName;
	private String roleName;
}
