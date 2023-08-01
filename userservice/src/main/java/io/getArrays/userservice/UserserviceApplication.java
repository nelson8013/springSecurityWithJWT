package io.getArrays.userservice;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.getArrays.userservice.Models.Role;
import io.getArrays.userservice.Models.User;
import io.getArrays.userservice.Service.UserService;

@SpringBootApplication
public class UserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
						userService.saveRole(new Role(null, "USER"));
						userService.saveRole(new Role(null, "MANAGER"));
						userService.saveRole(new Role(null, "ADMIN"));
						userService.saveRole(new Role(null, "SUPER_ADMIN"));

						userService.saveUser(new User(null, "John Travolta", "John", "1234", new ArrayList<>()));
						userService.saveUser(new User(null, "Will Smith", "will", "1234", new ArrayList<>()));
						userService.saveUser(new User(null, "Jim Carry", "Jim", "1234", new ArrayList<>()));
						userService.saveUser(new User(null, "Arnold Schwarzenegger", "arnold", "1234", new ArrayList<>()));


						userService.addRoleToUser("john", "USER");
						userService.addRoleToUser("john", "MANAGER");
						userService.addRoleToUser("will", "MANAGER");
						userService.addRoleToUser("Jim", "ADMIN");
						userService.addRoleToUser("arnold", "SUPER_ADMIN");
						userService.addRoleToUser("arnold", "ADMIN");
						userService.addRoleToUser("arnold", "USER");
		};
	}

}
