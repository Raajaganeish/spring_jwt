package com.secuiity.jwt;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.secuiity.jwt.entity.AppUser;
import com.secuiity.jwt.entity.Role;
import com.secuiity.jwt.service.UserService;

@SpringBootApplication
public class JwtApplication {
	private static final String ROLE_USER = "ROLE_USER";
	private static final String ROLE_MANAGER = "ROLE_MANAGER";
	private static final String ROLE_ADMIN = "ROLE_ADMIN";

	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userServiceImpl) {
		return args -> {
			userServiceImpl.save(new Role(null, ROLE_USER));
			userServiceImpl.save(new Role(null, ROLE_MANAGER));
			userServiceImpl.save(new Role(null, ROLE_ADMIN));

			userServiceImpl.save(new AppUser(null, "John", "John96", "1234", new ArrayList<>()));
			userServiceImpl.save(new AppUser(null, "Arjun", "Arjun5", "1234", new ArrayList<>()));
			userServiceImpl.save(new AppUser(null, "Smith", "Smith99", "1234", new ArrayList<>()));
			userServiceImpl.save(new AppUser(null, "Wayne", "Wayne12", "1234", new ArrayList<>()));
			userServiceImpl.save(new AppUser(null, "Bruce", "56Bruce", "1234", new ArrayList<>()));

			userServiceImpl.addRoleToAppUser("John96", ROLE_ADMIN);
			userServiceImpl.addRoleToAppUser("John96", ROLE_USER);
			userServiceImpl.addRoleToAppUser("Arjun5", ROLE_MANAGER);
			userServiceImpl.addRoleToAppUser("Smith99", ROLE_ADMIN);
			userServiceImpl.addRoleToAppUser("Wayne12", ROLE_USER);
			userServiceImpl.addRoleToAppUser("56Bruce", ROLE_MANAGER);

		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
